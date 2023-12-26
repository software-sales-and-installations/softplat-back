package ru.softplat.main.server.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.BankRequisitesMapper;
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
    private final BankRequisitesMapper mapper;

    @Transactional(readOnly = true)
    public BankRequisites getRequisites(long userId) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, BankRequisites.class)
            );
        return seller.getRequisites();
    }

    public BankRequisites updateRequisites(long userId, BankRequisites requisitesForUpdate) {
        Seller seller = sellerService.getSeller(userId);
        BankRequisites requisites = mapper.updateRequisites(seller.getRequisites(), requisitesForUpdate);
        return bankRepository.save(requisites);
    }

    public void deleteRequisites(long userId) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() == null)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, BankRequisites.class)
            );
        bankRepository.delete(seller.getRequisites());
    }

    public BankRequisites addRequisites(long userId, BankRequisites requisites) {
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() != null) throw new WrongConditionException("У этого продавца уже есть реквизиты");
        seller.setRequisites(requisites);
        bankRepository.save(requisites);
        return sellerRepository.save(seller).getRequisites();
    }
}
