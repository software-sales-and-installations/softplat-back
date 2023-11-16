package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
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
import ru.yandex.workshop.security.dto.UserCreateDto;
import ru.yandex.workshop.security.exception.WrongDataDbException;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;

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

    public User createNewUser(UserCreateDto userCreateDto) throws WrongDataDbException {
        if (checkIfUserExistsByEmail(userCreateDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userCreateDto.getEmail());

        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userCreateDto.setStatus(Status.ACTIVE);
        User user = User.builder().email(userCreateDto.getEmail()).role(userCreateDto.getRole()).status(userCreateDto.getStatus()).build();
        try {
            switch (userCreateDto.getRole()) {
                case ADMIN:
                    log.info(LogMessage.TRY_ADD_ADMIN.label);

                    Admin admin = adminService.addAdmin(userMapper.userDtoToAdmin(userCreateDto));
                    user.setId(admin.getId());
                    break;
                case SELLER:
                    log.info(LogMessage.TRY_ADD_SELLER.label);

                    Seller seller = sellerService.addSeller(userMapper.userDtoToSeller(userCreateDto));
                    user.setId(seller.getId());
                    break;
                case BUYER:
                    log.debug(LogMessage.TRY_ADD_BUYER.label);

                    Buyer buyer = buyerService.addBuyer(userMapper.userDtoToBuyer(userCreateDto));
                    user.setId(buyer.getId());
                    break;
            }
            repository.save(userMapper.userDtoToUser(userCreateDto));
        } catch (DataIntegrityViolationException e) {
            throw new WrongDataDbException(e.getMessage());
        }

        return user;
    }

    private boolean checkIfUserExistsByEmail(String email) {
        return (repository.findByEmail(email).isPresent());
    }
}