package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.service.admin.AdminService;
import ru.yandex.workshop.main.service.buyer.BuyerService;
import ru.yandex.workshop.main.service.seller.SellerService;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final AdminService adminService;
    private final SellerService sellerService;
    private final BuyerService buyerService;
    private final UserMapper userMapper;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    public User createNewUser(UserDto userDto) {
        if (checkIfUserExistsByEmail(userDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userDto.getEmail());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setStatus(Status.ACTIVE);
        User userSecutiry = repository.save(userMapper.userDtoToUser(userDto));
        User user = null;
        ResponseEntity<Object> response = null;

        switch (userDto.getRole()) {
            case ADMIN:
                log.info(LogMessage.TRY_ADD_ADMIN.label);

                Admin admin = adminService.addAdmin(userMapper.userDtoToAdmin(userDto));
                user = User.builder().id(admin.getId()).email(admin.getEmail()).role(userSecutiry.getRole()).status(userSecutiry.getStatus()).build();
                break;
            case SELLER:
                log.info(LogMessage.TRY_ADD_SELLER.label);

                Seller seller = sellerService.addSeller(userMapper.userDtoToSeller(userDto));
                user = User.builder().id(seller.getId()).email(seller.getEmail()).role(userSecutiry.getRole()).status(userSecutiry.getStatus()).build();
                break;
            case BUYER:
                log.debug(LogMessage.TRY_ADD_BUYER.label);

                Buyer buyer = buyerService.addBuyer(userMapper.userDtoToBuyer(userDto));
                user = User.builder().id(buyer.getId()).email(buyer.getEmail()).role(userSecutiry.getRole()).status(userSecutiry.getStatus()).build();
                break;
        }

        return user;

    }

    private boolean checkIfUserExistsByEmail(String email) {
        return (repository.findByEmail(email).isPresent());
    }
}