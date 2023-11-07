package ru.yandex.workshop.main.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.user.SellerDto;
import ru.yandex.workshop.main.dto.user.mapper.SellerMapper;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.dto.validation.ValidSeller;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.security.dto.UserDto;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final ImageService imageService;

    @Transactional
    public void addSeller(UserDto userDto) throws ValidationException {
        if (checkIfUserExistsByEmail(userDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userDto.getEmail());

        if(userDto.getPhone().isEmpty()) throw new ValidationException("Необходимо указать номер телефона. Телефонный номер должен начинаться с +7, затем - 10 цифр.");
        if(userDto.getDescription().isEmpty()) throw new ValidationException("Необходимо указать описание вашего профиля. Описание должно быть длинной не более 500 символов.");


        Seller seller = SellerMapper.INSTANCE.userDtoToSeller(userDto);
        seller.setRegistrationTime(LocalDateTime.now());

        sellerRepository.save(seller);
    }

    @Transactional
    public SellerResponseDto updateSeller(String email, SellerDto sellerForUpdate) {
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
        return (sellerRepository.findByEmail(email).isPresent());
    }
}
