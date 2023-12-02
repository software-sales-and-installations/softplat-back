package ru.softplat.main.server.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.seller.Seller;
import ru.softplat.main.server.repository.seller.SellerRepository;
import ru.softplat.main.server.service.image.ImageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final ImageService imageService;

    public Seller addSeller(Seller seller) {
        if (checkIfSellerExistsByEmail(seller.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + seller.getEmail());
//TODO должно быть в секьюрити только, можно удалить дублирование

        seller.setRegistrationTime(LocalDateTime.now());

        return sellerRepository.save(seller);
    }

    public Seller updateSeller(Long userId, Seller sellerForUpdate) {
        Seller seller = getSeller(userId);

        if (sellerForUpdate.getName() != null) seller.setName(sellerForUpdate.getName());
        /*if (sellerForUpdate.getEmail() != null) {
            if (checkIfSellerExistsByEmail(sellerForUpdate.getEmail()))
                throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + sellerForUpdate.getEmail());
            userDetailsChangeService.changeEmail(seller.getEmail(), sellerForUpdate.getEmail());
            seller.setEmail(sellerForUpdate.getEmail());
        }*/
        if (sellerForUpdate.getPhone() != null) seller.setPhone(sellerForUpdate.getPhone());

        return sellerRepository.save(seller);
    }

    public Seller addSellerImage(Long userId, MultipartFile file) {
        Seller seller = getSeller(userId);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
        seller.setImage(imageService.addNewImage(file));
        return seller;
    }

    public void deleteSellerImage(Long userId) {
        Seller seller = getSeller(userId);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
    }

    @Transactional(readOnly = true)
    public List<Seller> getAllSellers(int from, int size) {
        return sellerRepository.findAll(PageRequestOverride.of(from, size)).stream()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Seller getSeller(Long userId) {
        return sellerRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, Seller.class)
                ));
    }

    private boolean checkIfSellerExistsByEmail(String email) {
        return (sellerRepository.findByEmail(email).isPresent());
    }
}
