package ru.softplat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.exception.DuplicateException;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.message.LogMessage;
//import ru.softplat.model.admin.Admin;
//import ru.softplat.model.buyer.Buyer;
//import ru.softplat.model.seller.Seller;
//import ru.softplat.service.admin.AdminService;
//import ru.softplat.service.buyer.BuyerService;
//import ru.softplat.service.seller.SellerService;
import ru.softplat.dto.UserCreateDto;
import ru.softplat.mapper.UserMapper;
import ru.softplat.model.Status;
import ru.softplat.model.User;
import ru.softplat.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
//    private final AdminService adminService;
//    private final SellerService sellerService;
//    private final BuyerService buyerService;
    private final UserMapper userMapper;
    @Lazy
    private final PasswordEncoder passwordEncoder;

//    public User createNewUser(UserCreateDto userCreateDto) {
//        if (checkIfUserExistsByEmail(userCreateDto.getEmail()))
//            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userCreateDto.getEmail());
//
//        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
//        userCreateDto.setStatus(Status.ACTIVE);
//        User user = User.builder().email(userCreateDto.getEmail()).role(userCreateDto.getRole()).status(userCreateDto.getStatus()).build();
//        try {
//            switch (userCreateDto.getRole()) {
//                case ADMIN:
//                    log.info(LogMessage.TRY_ADD_ADMIN.label);
//
//                    Admin admin = adminService.addAdmin(userMapper.userDtoToAdmin(userCreateDto));
//                    user.setId(admin.getId());
//                    break;
//                case SELLER:
//                    log.info(LogMessage.TRY_ADD_SELLER.label);
//
//                    Seller seller = sellerService.addSeller(userMapper.userDtoToSeller(userCreateDto));
//                    user.setId(seller.getId());
//                    break;
//                case BUYER:
//                    log.debug(LogMessage.TRY_ADD_BUYER.label);
//
//                    Buyer buyer = buyerService.addBuyer(userMapper.userDtoToBuyer(userCreateDto));
//                    user.setId(buyer.getId());
//                    break;
//            }
//
//            repository.save(userMapper.userDtoToUser(userCreateDto));
//        } catch (DataIntegrityViolationException | UnexpectedRollbackException e) {
//            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userCreateDto.getPhone());
//        }
//
//        return user;
//    }

    private boolean checkIfUserExistsByEmail(String email) {
        return (repository.findByEmail(email).isPresent());
    }
}