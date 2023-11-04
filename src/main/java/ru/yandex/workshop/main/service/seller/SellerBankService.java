package ru.yandex.workshop.main.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.security.model.user.Seller;
import ru.yandex.workshop.main.repository.seller.BankRepository;
import ru.yandex.workshop.security.repository.SellerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SellerBankService {

    private final SellerRepository sellerRepository;
    private final BankRepository bankRepository;

    public BankRequisitesDto getRequisites(String email) {
        Seller seller = getSellerFromDatabase(email);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
        return new BankRequisitesDto(seller.getRequisites().getAccount());
    }

    @Transactional
    public BankRequisitesDto updateRequisites(String email, BankRequisitesDto requisites) {
        Seller seller = getSellerFromDatabase(email);
        seller.setRequisites(bankRepository.save(new BankRequisites(null, requisites.getAccount())));
        sellerRepository.save(seller);
        return new BankRequisitesDto(seller.getRequisites().getAccount());
    }

    @Transactional
    public void deleteRequisites(String email) {
        Seller seller = getSellerFromDatabase(email);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
        bankRepository.delete(seller.getRequisites());
        seller.setRequisites(null);
        sellerRepository.save(seller);
    }

    private Seller getSellerFromDatabase(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
