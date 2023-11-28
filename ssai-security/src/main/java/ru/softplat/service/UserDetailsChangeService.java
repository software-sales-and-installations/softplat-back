package ru.softplat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softplat.dto.JwtAuthRequest;
import ru.softplat.exception.WrongRegException;
import ru.softplat.mapper.UserMapper;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.model.User;
import ru.softplat.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsChangeService {
    private final UserRepository repository;
    private final UserMapper userMapper;
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
}
