package ru.softplat.security.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.client.user.AdminClient;
import ru.softplat.main.client.user.BuyerClient;
import ru.softplat.main.client.user.SellerClient;
import ru.softplat.security.server.dto.UserCreateDto;
import ru.softplat.security.server.exception.DuplicateException;
import ru.softplat.security.server.mapper.UserMapper;
import ru.softplat.security.server.message.ExceptionMessage;
import ru.softplat.security.server.message.LogMessage;
import ru.softplat.security.server.model.Status;
import ru.softplat.security.server.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final AdminClient adminClient;
    private final SellerClient sellerClient;
    private final BuyerClient buyerClient;
    private final UserMapper userMapper;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> createNewUser(UserCreateDto userCreateDto) {
        if (checkIfUserExistsByEmail(userCreateDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userCreateDto.getEmail());

        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userCreateDto.setStatus(Status.ACTIVE);
        ResponseEntity<Object> response = null;
        try {
            switch (userCreateDto.getRole()) {
                case ADMIN:
                    log.info(LogMessage.TRY_ADD_ADMIN.label);
                    response = adminClient.addAdmin(userMapper.userToUserMain(userCreateDto));
                    break;
                case SELLER:
                    log.info(LogMessage.TRY_ADD_SELLER.label);
                    response = sellerClient.createSeller(userMapper.userToUserMain(userCreateDto));
                    break;
                case BUYER:
                    log.debug(LogMessage.TRY_ADD_BUYER.label);
                    response = buyerClient.createBuyer(userMapper.userToUserMain(userCreateDto));
                    break;
            }

            repository.save(userMapper.userDtoToUser(userCreateDto));
        } catch (DataIntegrityViolationException | UnexpectedRollbackException e) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userCreateDto.getPhone());
        }

        return response;
    }

    private boolean checkIfUserExistsByEmail(String email) {
        return (repository.findByEmail(email).isPresent());
    }
}