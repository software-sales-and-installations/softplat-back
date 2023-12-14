package ru.softplat.security.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softplat.security.server.dto.JwtAuthRequest;
import ru.softplat.security.server.exception.EntityNotFoundException;
import ru.softplat.security.server.exception.WrongRegException;
import ru.softplat.security.server.message.ExceptionMessage;
import ru.softplat.security.server.model.Role;
import ru.softplat.security.server.model.Status;
import ru.softplat.security.server.model.User;
import ru.softplat.security.server.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsChangeService {
    private final UserRepository repository;
    @Lazy
    private final PasswordEncoder passwordEncoder;


    public User changePass(JwtAuthRequest request) throws WrongRegException {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new WrongRegException(ExceptionMessage.CONFIRMED_PASSWORD_EXCEPTION.label);
        }

        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new WrongRegException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }

    public void changeEmail(String oldEmail, String newEmail) {
        User user = repository.findByEmail(oldEmail).orElseThrow(() -> new WrongRegException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        user.setEmail(newEmail);
        repository.save(user);
    }

    public void bannedUser(long userId, Role role) {
        User user = findUsers(userId, role);

        if (user != null && user.getStatus().equals(Status.ACTIVE)) {
            user.setStatus(Status.BANNED);
            repository.save(user);
        }
    }

    public void unbannedUser(long userId, Role role) {
        User user = findUsers(userId, role);

        if (user != null && user.getStatus().equals(Status.BANNED)) {
            user.setStatus(Status.ACTIVE);
            repository.save(user);
        }
    }

    private User findUsers(long userId, Role role) {
        switch (role) {
            case BUYER:
                return repository.findByIdMainAndRole(userId, Role.BUYER).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
            case SELLER:
                return repository.findByIdMainAndRole(userId, Role.SELLER).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        }

        return null;
    }
}
