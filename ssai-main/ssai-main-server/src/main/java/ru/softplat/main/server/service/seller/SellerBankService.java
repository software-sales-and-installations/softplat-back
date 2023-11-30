package ru.softplat.main.server.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.seller.BankRequisites;
import ru.softplat.main.server.model.seller.Seller;
import ru.softplat.main.server.repository.seller.BankRepository;
import ru.softplat.main.server.repository.seller.SellerRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SellerBankService {

    private final SellerRepository sellerRepository;
    private final SellerService sellerService;
    private final BankRepository bankRepository;

    @Transactional(readOnly = true)
    public BankRequisites getRequisites(long userId) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, BankRequisites.class)
            );
        return seller.getRequisites();
    }

    public BankRequisites updateRequisites(long userId, BankRequisites requisites) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() != null) {
            bankRepository.deleteById(seller.getRequisites().getId());
        }
        seller.setRequisites(bankRepository.save(new BankRequisites(null, requisites.getAccount())));
        return sellerRepository.save(seller).getRequisites();
    }

    public void deleteRequisites(long userId) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, BankRequisites.class)
            );
        bankRepository.delete(seller.getRequisites());
        seller.setRequisites(null);
        sellerRepository.save(seller);
    }
}
