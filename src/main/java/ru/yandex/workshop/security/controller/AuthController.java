package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/auth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Выход пользователя");
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        log.info("Регистрация пользователя");
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping("/registration/admin")
    public ResponseEntity<Object> createNewAdmin(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        log.info("Регистрация администратора");
        return authService.createNewUser(registrationUserDto);
    }
}
