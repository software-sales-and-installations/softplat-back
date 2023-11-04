package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.security.dto.registration.RegistrationAdminDto;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.exception.WrongRegException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminDetailsServiceImpl adminService;
    private final SellerDetailsServiceImpl sellerService;
    private final BuyerDetailsServiceImpl buyerService;

    public ResponseEntity<Object> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new WrongRegException("Пароли не совпадают");
        }

        switch (registrationUserDto.getRole()) {
            case SELLER:
                if (sellerService.checkIfUserExistsByEmail(registrationUserDto.getEmail())) {
                    throw new WrongRegException("Пользователь с указанным логином уже существует");
                } else {
                    return ResponseEntity.of(Optional.ofNullable(sellerService.addSeller(registrationUserDto)));
                }
            case BUYER:
                if (buyerService.checkIfUserExistsByEmail(registrationUserDto.getEmail())) {
                    throw new WrongRegException("Пользователь с указанным логином уже существует");
                } else {
                    return ResponseEntity.of(Optional.ofNullable(buyerService.addNewBuyer(registrationUserDto)));

                }
        }

        throw new WrongRegException("Ошибка при регистрации");
    }

    public ResponseEntity<Object> createNewAdmin(RegistrationAdminDto registrationAdminDto) {
        if (!registrationAdminDto.getPassword().equals(registrationAdminDto.getConfirmPassword())) {
            throw new WrongRegException("Пароли не совпадают");
        }

        if (adminService.getAdmin(registrationAdminDto.getEmail()) != null) {
            throw new WrongRegException("Пользователь с указанным логином уже существует");
        } else {
            return ResponseEntity.of(Optional.ofNullable(adminService.addAdmin(registrationAdminDto)));
        }

    }
}