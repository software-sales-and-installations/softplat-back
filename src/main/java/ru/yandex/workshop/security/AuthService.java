package ru.yandex.workshop.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.main.dto.buyer.BuyerDto;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.service.buyer.BuyerService;
import ru.yandex.workshop.main.service.seller.SellerService;
import ru.yandex.workshop.security.authConfig.JwtTokenUtils;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.dto.JwtResponse;
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.exception.UnauthorizedException;
import ru.yandex.workshop.security.exception.WrongRegException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAuthService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final SellerService sellerService;
    private final BuyerService buyerService;

    public ResponseEntity<Object> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<Object> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new WrongRegException("Пароли не совпадают");
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            throw  new WrongRegException("Пользователь с указанным именем уже существует");
        }
        userService.createNewUser(registrationUserDto);
        switch (registrationUserDto.getRole()) {
            case SELLER:
                return ResponseEntity.of(Optional.ofNullable(sellerService.addSeller(new SellerDto(registrationUserDto.getName(),
                        registrationUserDto.getEmail(), registrationUserDto.getPhone()))));
            case BUYER:
                return ResponseEntity.of(Optional.ofNullable(buyerService.addNewBuyer(new BuyerDto(
                        registrationUserDto.getEmail(), registrationUserDto.getName(), null, //TODO remove last name
                        registrationUserDto.getPhone()))));
        }
        throw new WrongRegException("Ошибка при регистрации");
    }
}