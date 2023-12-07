package ru.softplat.main.server.service.seller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.dto.seller.LegalForm;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;
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

    public BankRequisites updateRequisites(long userId, BankRequisites requisitesForUpdate) {
        Seller seller = sellerService.getSeller(userId);
        BankRequisites requisites = seller.getRequisites();
        if (requisitesForUpdate.getInn() != null) requisites.setInn(requisitesForUpdate.getInn());
        if (requisitesForUpdate.getLegalForm() != null) {
            requisites.setLegalForm(requisitesForUpdate.getLegalForm());
            if (requisites.getLegalForm() == LegalForm.IP) requisites.setOgrn(null);
            else requisites.setOgrnip(null);
        }
        if (requisitesForUpdate.getAccount() != null) requisites.setAccount(requisitesForUpdate.getAccount());
        if (requisitesForUpdate.getBik() != null) requisites.setBik(requisitesForUpdate.getBik());
        if (requisitesForUpdate.getKpp() != null) requisites.setKpp(requisitesForUpdate.getKpp());
        if (requisitesForUpdate.getOgrn() != null) requisites.setOgrn(requisitesForUpdate.getOgrn());
        if (requisitesForUpdate.getOgrnip() != null) requisites.setOgrnip(requisitesForUpdate.getOgrnip());
        if (requisitesForUpdate.getAddress() != null) requisites.setAddress(requisitesForUpdate.getAddress());
        validateRequisites(requisites);
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
        validateRequisites(requisites);
        Seller seller = sellerService.getSeller(userId);
        if (seller.getRequisites() != null) throw new WrongConditionException("У этого продавца уже есть реквизиты");
        seller.setRequisites(requisites);
        bankRepository.save(requisites);
        return sellerRepository.save(seller).getRequisites();
    }

    private void validateRequisites(BankRequisites requisites) {
        if (requisites.getLegalForm() == LegalForm.IP) {
            if (requisites.getInn().length() != 12)
                throw new WrongConditionException("Длина ИНН должна быть 12 цифр");
            if (requisites.getOgrnip() == null || requisites.getOgrnip().isBlank())
                throw new WrongConditionException("Введите корректный ОГРНИП");
        } else {
            if (requisites.getInn().length() != 10)
                throw new WrongConditionException("Длина ИНН должна быть 10 цифр");
            if (requisites.getOgrn() == null || requisites.getOgrn().isBlank())
                throw new WrongConditionException("Введите корректный ОГРН");
        }
    }
}
