package ru.yandex.workshop.security.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.mapper.SellerMapper;
import ru.yandex.workshop.security.model.Seller;
import ru.yandex.workshop.security.repository.SellerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerService implements UserDetailsService {

    private final SellerRepository sellerRepository;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Seller seller = getSellerFromDatabase(email);
        return SellerResponseDto.fromUser(seller);
    }

    @Transactional
    public SellerResponseDto addSeller(RegistrationUserDto registrationUserDto) {
        getSellerFromDatabase(registrationUserDto.getEmail());
        Seller seller = Seller.builder()
                .email(registrationUserDto.getEmail())
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .name(registrationUserDto.getName())
                .phone(registrationUserDto.getNumber())
                .registrationTime(LocalDateTime.now())
                .role(registrationUserDto.getRole())
                .build();
        try {
            return SellerMapper.INSTANCE.sellerToSellerForResponse(sellerRepository.save(seller));
        } catch (Exception e) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }

    @Transactional
    public SellerResponseDto updateSeller(String email, SellerForUpdate sellerForUpdate) {
        Seller seller = getSellerFromDatabase(email);
        if (sellerForUpdate.getName() != null) seller.setName(sellerForUpdate.getName());
        if (sellerForUpdate.getDescription() != null) seller.setDescription(sellerForUpdate.getDescription());
        if (sellerForUpdate.getEmail() != null && !sellerRepository.findByEmail(sellerForUpdate.getEmail()).isPresent())
//        if (sellerForUpdate.getEmail() != null && sellerRepository.findByEmail(sellerForUpdate.getEmail()).isEmpty())
            seller.setEmail(sellerForUpdate.getEmail());
        //TODO save image
        if (sellerForUpdate.getPhone() != null) seller.setPhone(sellerForUpdate.getPhone());
        return SellerMapper.INSTANCE.sellerToSellerForResponse(sellerRepository.save(seller));
    }


    //TODO add in any controller
    public List<SellerResponseDto> getAllSellers() {
        return sellerRepository.findAll(Pageable.ofSize(10)).stream()//TODO common custom pagination
                .map(SellerMapper.INSTANCE::sellerToSellerForResponse)
                .collect(Collectors.toList());
    }

    public SellerResponseDto getSeller(String email) {
        return SellerMapper.INSTANCE.sellerToSellerForResponse(getSellerFromDatabase(email));
    }

    private Seller getSellerFromDatabase(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
