package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.main.controller.user.AdminController;
import ru.yandex.workshop.main.controller.user.BuyerController;
import ru.yandex.workshop.main.controller.user.SellerController;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.exception.WrongRegException;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final AdminController adminController;
    private final SellerController sellerController;
    private final BuyerController buyerController;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> createNewUser(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new WrongRegException("Пароли не совпадают");
        }

        if (repository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new WrongRegException("Пользователь с указанным логином уже существует");
        } else {
            User user = UserMapper.INSTANCE.userDtoToUser(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            switch (userDto.getRole()) {
                case ADMIN:
                    adminController.addAdmin(userDto);
                    break;
                case SELLER:
                    sellerController.addSeller(userDto);
                    break;
                case BUYER:
                    buyerController.addBuyer(userDto);
                    break;
            }

            return ResponseEntity.of(Optional.of(repository.save(user)));
        }

    }
}