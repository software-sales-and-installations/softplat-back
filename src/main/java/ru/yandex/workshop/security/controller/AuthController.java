package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.security.config.JwtTokenProvider;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.dto.registration.RegistrationAdminDto;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.service.AdminDetailsServiceImpl;
import ru.yandex.workshop.security.service.AuthService;
import ru.yandex.workshop.security.service.BuyerDetailsServiceImpl;
import ru.yandex.workshop.security.service.SellerDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminDetailsServiceImpl adminDetailsService;
    private final SellerDetailsServiceImpl sellerDetailsService;
    private final BuyerDetailsServiceImpl buyerDetailsService;


    @PostMapping("/auth/login")
    public ResponseEntity<Object> authorization(@RequestBody JwtRequest request) {
        log.info("Авторизация пользователя");

        try {
            Map<Object, Object> response = new HashMap<>();
            String token;
            switch (request.getRole()) {
                case ADMIN:
                    UserDetails admin = adminDetailsService.loadUserByUsername(request.getEmail());
                    token = jwtTokenProvider.generateToken(request.getEmail(), request.getRole().name());
                    response.put("email", request.getEmail());
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                case SELLER:
                    UserDetails seller = sellerDetailsService.loadUserByUsername(request.getEmail());
                    token = jwtTokenProvider.generateToken(request.getEmail(), request.getRole().name());
                    response.put("email", request.getEmail());
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                case BUYER:
                    UserDetails buyer = buyerDetailsService.loadUserByUsername(request.getEmail());
                    token = jwtTokenProvider.generateToken(request.getEmail(), request.getRole().name());
                    response.put("email", request.getEmail());
                    response.put("token", token);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
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
    public ResponseEntity<Object> createNewAdmin(@RequestBody @Valid RegistrationAdminDto registrationAdminDto) {
        log.info("Регистрация администратора");
        return authService.createNewAdmin(registrationAdminDto);
    }
}
