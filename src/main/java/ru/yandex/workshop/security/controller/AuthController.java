package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            switch (request.getRole()) {
                case ADMIN:
                    UserDetails admin = adminDetailsService.loadUserByUsername(request.getEmail());
                    token = jwtTokenProvider.generateToken(request.getEmail(), request.getRole().name());
                    response.put("email", admin.getUsername());
                    response.put("token", token);
                    break;
                case SELLER:
                    UserDetails seller = sellerDetailsService.loadUserByUsername(request.getEmail());
                    token = jwtTokenProvider.generateToken(request.getEmail(), request.getRole().name());
                    response.put("email", seller.getUsername());
                    response.put("token", token);
                    break;
                case BUYER:
                    UserDetails buyer = buyerDetailsService.loadUserByUsername(request.getEmail());
                    token = jwtTokenProvider.generateToken(request.getEmail(), request.getRole().name());
                    response.put("email", buyer.getUsername());
                    response.put("token", token);
                    break;
            }

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Неправильный email/password", HttpStatus.FORBIDDEN);
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
