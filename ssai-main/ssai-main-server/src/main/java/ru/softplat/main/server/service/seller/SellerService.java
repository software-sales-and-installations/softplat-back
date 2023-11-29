package ru.softplat.main.server.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.server.repository.seller.SellerRepository;
import ru.softplat.main.server.service.image.ImageService;
import ru.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.seller.Seller;

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
    //private final UserDetailsChangeService userDetailsChangeService;

    public Seller addSeller(Seller seller) {
        if (checkIfSellerExistsByEmail(seller.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + seller.getEmail());

        seller.setRegistrationTime(LocalDateTime.now());

        return sellerRepository.save(seller);
    }

    public Seller updateSeller(String email, Seller sellerForUpdate) {
        Seller seller = getSeller(email);

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

    public Seller addSellerImage(String email, MultipartFile file) {
        Seller seller = getSeller(email);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
        seller.setImage(imageService.addNewImage(file));
        return seller;
    }

    public void deleteSellerImage(String email) {
        Seller seller = getSeller(email);
        if (seller.getImage() != null) {
            imageService.deleteImageById(seller.getImage().getId());
        }
    }

    public void deleteSellerImageBySellerId(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(sellerId), Seller.class)
                ));
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
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(userId), Seller.class)
                ));
    }

    @Transactional(readOnly = true)
    public Seller getSeller(String email) {
        return sellerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(email, Seller.class)
                ));
    }

    private boolean checkIfSellerExistsByEmail(String email) {
        return (sellerRepository.findByEmail(email).isPresent());
    }
}
