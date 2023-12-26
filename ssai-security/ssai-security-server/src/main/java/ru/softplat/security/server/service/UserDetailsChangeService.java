package ru.softplat.security.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.security.server.dto.ChangePassRequest;
import ru.softplat.security.server.exception.EntityNotFoundException;
import ru.softplat.security.server.exception.WrongRegException;
import ru.softplat.security.server.message.ExceptionMessage;
import ru.softplat.security.server.model.ResetToken;
import ru.softplat.security.server.model.Role;
import ru.softplat.security.server.model.Status;
import ru.softplat.security.server.model.User;
import ru.softplat.security.server.repository.ConfirmationTokenRepository;
import ru.softplat.security.server.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsChangeService {
    private final UserRepository repository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    public User changePass(ChangePassRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new WrongRegException(ExceptionMessage.CONFIRMED_PASSWORD_EXCEPTION.label);
        }

        User user = getUser(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }

    public void confirmResetToken(String token, ChangePassRequest authRequest) {
        ResetToken resetToken = confirmationTokenRepository.findByConfirmationToken(token);

        if (!resetToken.getUser().getEmail().equals(authRequest.getEmail()))
            throw new WrongRegException(ExceptionMessage.NOT_VALID_TOKEN.label);
        if (Duration.between(LocalDateTime.now(), resetToken.getCreatedOn()).toHours() > 24)
            throw new WrongRegException(ExceptionMessage.TIMEOUT_TOKEN.label);

        confirmationTokenRepository.deleteByConfirmationToken(token);
    }

    public String createResetToken(String email) {
        User user = getUser(email);
        ResetToken resetToken = new ResetToken(user);

        return confirmationTokenRepository.save(resetToken).getConfirmationToken();
    }

    private User getUser(String email) {
        return repository.findByEmail(email).orElseThrow(() ->
                new WrongRegException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(email)));
    }

    public void banUser(long userId, Role role) {
        User user = findUsers(userId, role);

        if (user.getStatus().equals(Status.ACTIVE)) {
            user.setStatus(Status.BANNED);
            repository.save(user);
        }
    }

    public void unbanUser(long userId, Role role) {
        User user = findUsers(userId, role);

        if (user.getStatus().equals(Status.BANNED)) {
            user.setStatus(Status.ACTIVE);
            repository.save(user);
        }
    }

    private User findUsers(long userId, Role role) {
        switch (role) {
            case BUYER:
                return repository.findByIdMainAndRole(userId, Role.BUYER).orElseThrow(() -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(role.toString())));
            case SELLER:
                return repository.findByIdMainAndRole(userId, Role.SELLER).orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(role.toString())));
            default:
                throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(role.toString()));
        }
    }
}
