package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.security.config.JwtTokenUtils;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.dto.JwtResponse;
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.exception.UnauthorizedException;
import ru.yandex.workshop.security.exception.WrongRegException;
import ru.yandex.workshop.security.service.user.AdminService;
import ru.yandex.workshop.security.service.user.BuyerService;
import ru.yandex.workshop.security.service.user.SellerService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;
    private final SellerService sellerService;
    private final BuyerService buyerService;

    public ResponseEntity<Object> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Неправильный логин или пароль");
        }

        UserDetails userDetails = null;
        String token = null;

        switch (authRequest.getRole()) {
            case ADMIN:
                userDetails = adminService.loadUserByUsername(authRequest.getEmail());
                token = jwtTokenUtils.generateToken(userDetails);
            case SELLER:
                userDetails = sellerService.loadUserByUsername(authRequest.getEmail());
                token = jwtTokenUtils.generateToken(userDetails);
            case BUYER:
                userDetails = buyerService.loadUserByUsername(authRequest.getEmail());
                token = jwtTokenUtils.generateToken(userDetails);
        }

        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<Object> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new WrongRegException("Пароли не совпадают");
        }

        switch (registrationUserDto.getRole()) {
            case ADMIN:
                if (adminService.getAdmin(registrationUserDto.getEmail()) != null) {
                    throw new WrongRegException("Пользователь с указанным логином уже существует");
                } else {
                    return ResponseEntity.of(Optional.ofNullable(adminService.addAdmin(registrationUserDto)));
                }
            case SELLER:
                if (sellerService.getSeller(registrationUserDto.getEmail()) != null) {
                    throw new WrongRegException("Пользователь с указанным логином уже существует");
                } else {
                    return ResponseEntity.of(Optional.ofNullable(sellerService.addSeller(registrationUserDto)));
                }
            case BUYER:
                if (buyerService.getBuyer(registrationUserDto.getEmail()) != null) {
                    throw new WrongRegException("Пользователь с указанным логином уже существует");
                } else {
                    return ResponseEntity.of(Optional.ofNullable(buyerService.addNewBuyer(registrationUserDto)));

                }
        }

        throw new WrongRegException("Ошибка при регистрации");
    }
}