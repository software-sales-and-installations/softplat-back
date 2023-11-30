package ru.softplat.main.server.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.repository.seller.BankRepository;
import ru.softplat.main.server.repository.seller.SellerRepository;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.seller.BankRequisites;
import ru.softplat.main.server.model.seller.Seller;

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