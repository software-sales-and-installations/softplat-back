package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.security.AuthService;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.dto.RegistrationUserDto;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> createAuthToken(@RequestBody JwtRequest authRequest) {
        log.info("Авторизация пользователя");
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        log.info("Регистрация пользователя");
        return authService.createNewUser(registrationUserDto);
    }
}
