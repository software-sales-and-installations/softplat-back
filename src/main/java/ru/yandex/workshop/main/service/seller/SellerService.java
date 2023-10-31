package ru.yandex.workshop.main.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.dto.seller.SellerForResponse;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.dto.seller.SellerMapper;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.UserNotFoundException;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.SellerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    //TODO add in any controller
    public List<SellerForResponse> getAllSellers() {
        return sellerRepository.findAll(Pageable.ofSize(10)).stream()//TODO common custom pagination
                .map(SellerMapper.INSTANCE::sellerToSellerForResponse)
                .collect(Collectors.toList());
    }

    public SellerForResponse getSeller(String email) {
        return SellerMapper.INSTANCE.sellerToSellerForResponse(getSellerFromDatabase(email));
    }

    @Transactional
    public SellerForResponse addSeller(SellerDto sellerDto) {
        Seller seller = SellerMapper.INSTANCE.sellerDtoToSeller(sellerDto);
        seller.setRegistrationTime(LocalDateTime.now());
        try {
            return SellerMapper.INSTANCE.sellerToSellerForResponse(sellerRepository.save(seller));
        } catch (Exception e) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
    }

    @Transactional
    public SellerForResponse updateSeller(String email, SellerForUpdate sellerForUpdate) {
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

    private Seller getSellerFromDatabase(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует"));
    }
}
