package ru.softplat.security.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.security.server.dto.JwtAuthRequest;
import ru.softplat.security.server.exception.WrongRegException;
import ru.softplat.security.server.message.ExceptionMessage;
import ru.softplat.security.server.model.ResetToken;
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

    public User changePass(JwtAuthRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new WrongRegException(ExceptionMessage.CONFIRMED_PASSWORD_EXCEPTION.label);
        }

        User user = getUser(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }

    public void validateResetToken(String token, JwtAuthRequest authRequest) {
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
                new WrongRegException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
