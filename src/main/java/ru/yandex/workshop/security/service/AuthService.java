package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.admin.AdminService;
import ru.yandex.workshop.main.service.buyer.BuyerService;
import ru.yandex.workshop.main.service.seller.SellerService;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final AdminService adminService;
    private final SellerService sellerService;
    private final BuyerService buyerService;
    private final UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createNewUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setStatus(Status.ACTIVE);

        switch (userDto.getRole()) {
            case ADMIN:
                log.info(LogMessage.TRY_ADD_ADMIN.label);

                adminService.addAdmin(userMapper.userDtoToAdmin(userDto));
                break;
            case SELLER:
                log.info(LogMessage.TRY_ADD_SELLER.label);

                sellerService.addSeller(userMapper.userDtoToSeller(userDto));
                break;
            case BUYER:
                log.debug(LogMessage.TRY_ADD_BUYER.label);

                buyerService.addBuyer(userMapper.userDtoToBuyer(userDto));
                break;
        }

        return repository.save(userMapper.userDtoToUser(userDto));
    }
}