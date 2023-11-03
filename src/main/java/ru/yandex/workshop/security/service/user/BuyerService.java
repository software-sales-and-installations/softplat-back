package ru.yandex.workshop.security.service.user;

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
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.BuyerResponseDto;
import ru.yandex.workshop.security.dto.user.BuyerDto;
import ru.yandex.workshop.security.mapper.BuyerMapper;
import ru.yandex.workshop.security.model.Buyer;
import ru.yandex.workshop.security.repository.BuyerRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BuyerService implements UserDetailsService {

    private final BuyerRepository buyerRepository;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Buyer buyer = getBuyerOrThrowExceptionIfNotFound(email);
        return BuyerResponseDto.fromUser(buyer);
    }

    @Transactional
    public BuyerResponseDto addNewBuyer(RegistrationUserDto registrationUserDto) {
        checkIfUserExistsByEmail(registrationUserDto.getEmail());
        Buyer buyer = Buyer.builder()
                .email(registrationUserDto.getEmail())
                .name(registrationUserDto.getName())
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .registrationTime(LocalDateTime.now())
                .role(registrationUserDto.getRole()).build();
        Buyer response = buyerRepository.save(buyer);
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(response);
    }

    @Transactional(readOnly = true)
    public BuyerResponseDto getBuyer(String email) {
        Buyer response = getBuyerOrThrowExceptionIfNotFound(email);
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(response);
    }

    @Transactional
    public BuyerResponseDto updateBuyer(String email, BuyerDto updateDto) {
        Buyer oldBuyer = getBuyerOrThrowExceptionIfNotFound(email);
        Buyer updatedBuyer = updateBuyer(oldBuyer, updateDto);
        buyerRepository.save(updatedBuyer);
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(updatedBuyer);
    }

    private void checkIfUserExistsByEmail(String email) {
        if (buyerRepository.existsBuyerByEmail(email)) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + email);
        }
    }

    private Buyer getBuyerOrThrowExceptionIfNotFound(String email) {
        return buyerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
        );
    }

    private Buyer updateBuyer(Buyer oldBuyer, BuyerDto updateDto) {
        if (updateDto.getName() != null) {
            oldBuyer.setName(updateDto.getName());
        }
        if (updateDto.getEmail() != null) {
            checkIfUserExistsByEmail(updateDto.getEmail());
            oldBuyer.setEmail(updateDto.getEmail());
        }
        if (updateDto.getTelephone() != null) {
            oldBuyer.setTelephone(updateDto.getTelephone());
        }
        return oldBuyer;
    }
}
