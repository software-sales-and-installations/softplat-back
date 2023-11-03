package ru.yandex.workshop.main.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.dto.seller.SellerResponseDto;
import ru.yandex.workshop.main.dto.seller.SellerUpdateDto;
import ru.yandex.workshop.main.dto.seller.SellerMapper;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.service.image.ImageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ImageService imageService;

    //TODO add in any controller
    public List<SellerResponseDto> getAllSellers() {
        return sellerRepository.findAll(Pageable.ofSize(10)).stream()//TODO common custom pagination
                .map(SellerMapper.INSTANCE::sellerToSellerForResponse)
                .collect(Collectors.toList());
    }

    public SellerResponseDto getSeller(String email) {
        return SellerMapper.INSTANCE.sellerToSellerForResponse(getSellerFromDatabase(email));
    }

    @Transactional
    public SellerResponseDto addSeller(SellerDto sellerDto) {
        Seller seller = SellerMapper.INSTANCE.sellerDtoToSeller(sellerDto);
        seller.setRegistrationTime(LocalDateTime.now());
        try {
            return SellerMapper.INSTANCE.sellerToSellerForResponse(sellerRepository.save(seller));
        } catch (Exception e) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }

    @Transactional
    public SellerResponseDto addSellerImage(String email, MultipartFile file) {
        Seller seller = getSellerFromDatabase(email);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        seller.setImage(ImageMapper.INSTANCE.imageDtoToImage(imageDto));
        return SellerMapper.INSTANCE.sellerToSellerForResponse(seller);
    }

    @Transactional
    public SellerResponseDto updateSeller(String email, SellerUpdateDto sellerUpdateDto) {
        Seller seller = getSellerFromDatabase(email);
        if (sellerUpdateDto.getName() != null) seller.setName(sellerUpdateDto.getName());
        if (sellerUpdateDto.getDescription() != null) seller.setDescription(sellerUpdateDto.getDescription());
        if (sellerUpdateDto.getEmail() != null && !sellerRepository.findByEmail(sellerUpdateDto.getEmail()).isPresent())
            seller.setEmail(sellerUpdateDto.getEmail());
        if (sellerUpdateDto.getPhone() != null) seller.setPhone(sellerUpdateDto.getPhone());
        return SellerMapper.INSTANCE.sellerToSellerForResponse(sellerRepository.save(seller));
    }

    private Seller getSellerFromDatabase(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
