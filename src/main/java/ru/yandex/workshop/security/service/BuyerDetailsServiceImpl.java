package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.security.dto.UserSecurity;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.BuyerResponseDto;
import ru.yandex.workshop.security.dto.user.BuyerDto;
import ru.yandex.workshop.security.mapper.BuyerMapper;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.user.Buyer;
import ru.yandex.workshop.security.repository.BuyerRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BuyerDetailsServiceImpl implements UserDetailsService {

    private final BuyerRepository buyerRepository;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Buyer buyer = getSecurityBuyer(email);
        return UserSecurity.fromUser(buyer);
    }

    @Transactional
    public BuyerResponseDto addNewBuyer(RegistrationUserDto registrationUserDto) {
        if (checkIfUserExistsByEmail(registrationUserDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + registrationUserDto.getEmail());

        Buyer buyer = BuyerMapper.INSTANCE.buyerDtoToBuyer(registrationUserDto);
        buyer.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        buyer.setRegistrationTime(LocalDateTime.now());
        buyer.setStatus(Status.ACTIVE);

        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyerRepository.save(buyer));
    }

    @Transactional
    public BuyerResponseDto updateBuyer(String email, BuyerDto updateDto) {
        Buyer oldBuyer = getSecurityBuyer(email);

        if (updateDto.getName() != null) {
            oldBuyer.setName(updateDto.getName());
        }
        if (updateDto.getEmail() != null) {
            if (checkIfUserExistsByEmail(updateDto.getEmail()))
                throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + updateDto.getEmail());
            oldBuyer.setEmail(updateDto.getEmail());
        }
        if (updateDto.getPhone() != null) {
            oldBuyer.setPhone(updateDto.getPhone());
        }

        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyerRepository.save(oldBuyer));
    }

    public BuyerResponseDto getBuyer(Long userId) {
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    public Buyer getSecurityBuyer(String email) {
        return buyerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    public boolean checkIfUserExistsByEmail(String email) {
        return buyerRepository.findByEmail(email).isPresent();
    }
}
