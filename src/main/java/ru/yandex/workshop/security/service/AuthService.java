package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.admin.AdminService;
import ru.yandex.workshop.main.service.buyer.BuyerService;
import ru.yandex.workshop.main.service.seller.SellerService;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.exception.WrongRegException;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.message.ExceptionMessage;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final AdminService adminService;
    private final SellerService sellerService;
    private final BuyerService buyerService;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> createNewUser(UserDto userDto) throws ValidationException {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new WrongRegException(ExceptionMessage.CONFIRMED_PASSWORD_EXCEPTION.label);
        }

        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(Status.ACTIVE);

        switch (userDto.getRole()) {
            case ADMIN:
                log.info(LogMessage.TRY_ADD_ADMIN.label);

                adminService.addAdmin(userDto);
                break;
            case SELLER:
                log.info(LogMessage.TRY_ADD_SELLER.label);

                sellerService.addSeller(userDto);
                break;
            case BUYER:
                log.debug(LogMessage.TRY_ADD_BUYER.label);

                buyerService.addBuyer(userDto);
                break;
        }

        return ResponseEntity.of(Optional.of(UserMapper.INSTANCE.userToUserResponseDto(repository.save(user))));
    }
}