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
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.security.dto.UserSecurity;
import ru.yandex.workshop.security.dto.registration.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.mapper.SellerMapper;
import ru.yandex.workshop.security.model.Status;
import ru.yandex.workshop.security.model.user.Seller;
import ru.yandex.workshop.security.repository.SellerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerDetailsServiceImpl implements UserDetailsService {

    private final SellerRepository sellerRepository;
    private PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Seller seller = getSeller(email);
        return UserSecurity.fromUser(seller);
    }

    @Transactional
    public SellerResponseDto addSeller(RegistrationUserDto registrationUserDto) {
        if (checkIfUserExistsByEmail(registrationUserDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + registrationUserDto.getEmail());

        Seller seller = SellerMapper.INSTANCE.regSellerDtoToSeller(registrationUserDto);
        seller.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        seller.setRegistrationTime(LocalDateTime.now());
        seller.setStatus(Status.ACTIVE);

        return SellerMapper.INSTANCE.sellerToSellerResponseDto(sellerRepository.save(seller));
    }

    @Transactional
    public SellerResponseDto updateSeller(String email, SellerForUpdate sellerForUpdate) {
        Seller seller = getSeller(email);

        if (sellerForUpdate.getName() != null) seller.setName(sellerForUpdate.getName());
        if (sellerForUpdate.getDescription() != null) seller.setDescription(sellerForUpdate.getDescription());
        if (sellerForUpdate.getEmail() != null) {
            if (checkIfUserExistsByEmail(sellerForUpdate.getEmail()))
                throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + sellerForUpdate.getEmail());
            seller.setEmail(sellerForUpdate.getEmail());
        }
        //TODO save image
        if (sellerForUpdate.getPhone() != null) seller.setPhone(sellerForUpdate.getPhone());

        return SellerMapper.INSTANCE.sellerToSellerResponseDto(sellerRepository.save(seller));
    }

    @Transactional
    public SellerResponseDto addSellerImage(String email, MultipartFile file) {
        Seller seller = getSeller(email);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        seller.setImage(ImageMapper.INSTANCE.imageDtoToImage(imageDto));
        return SellerMapper.INSTANCE.sellerToSellerResponseDto(seller);
    }

    @Transactional
    public void deleteSellerImage(String email) {
        Seller seller = getSeller(email);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
    }

    public void deleteSellerImageBySellerId(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
    }

    public List<SellerResponseDto> getAllSellers(int from, int size) {
        return sellerRepository.findAll(PageRequestOverride.of(from, size)).stream()
                .map(SellerMapper.INSTANCE::sellerToSellerResponseDto)
                .collect(Collectors.toList());
    }

    public SellerResponseDto getSellerDto(Long userId) {
        return SellerMapper.INSTANCE.sellerToSellerResponseDto(sellerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    public Seller getSeller(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    public boolean checkIfUserExistsByEmail(String email) {
        return  (sellerRepository.findByEmail(email).isPresent());
    }
}
