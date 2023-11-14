package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.exception.WrongRegException;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.message.ExceptionMessage;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsChangeService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    @Lazy
    private final PasswordEncoder passwordEncoder;


    public User changePass(JwtRequest request) throws WrongRegException {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new WrongRegException(ExceptionMessage.CONFIRMED_PASSWORD_EXCEPTION.label);
        }

        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new WrongRegException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }

    public void changeEmail(String oldEmail, String newEmail) {
        User user = repository.findByEmail(oldEmail).orElseThrow(() -> new WrongRegException(ru.yandex.workshop.security.message.ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        user.setEmail(newEmail);
        repository.save(user);
    }
}
