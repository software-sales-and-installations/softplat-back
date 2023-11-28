package ru.softplat.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.exception.EntityNotFoundException;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.model.seller.BankRequisites;
import ru.softplat.model.seller.Seller;
import ru.softplat.repository.seller.BankRepository;
import ru.softplat.repository.seller.SellerRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SellerBankService {

    private final SellerRepository sellerRepository;
    private final SellerService sellerService;
    private final BankRepository bankRepository;

    @Transactional(readOnly = true)
    public BankRequisites getRequisites(Long userId) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(userId), BankRequisites.class)
            );
        return seller.getRequisites();
    }

    public BankRequisites updateRequisites(String email, BankRequisites requisites) {
        Seller seller = sellerService.getSeller(email);
        if (seller.getRequisites() != null) {
            bankRepository.deleteById(seller.getRequisites().getId());
        }
        seller.setRequisites(bankRepository.save(new BankRequisites(null, requisites.getAccount())));
        return sellerRepository.save(seller).getRequisites();
    }

    public void deleteRequisites(String email) {
        Seller seller = sellerService.getSeller(email);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(email, BankRequisites.class)
            );
        bankRepository.delete(seller.getRequisites());
        seller.setRequisites(null);
        sellerRepository.save(seller);
    }
}
