package ru.yandex.workshop.main.service.seller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.exception.UserNotFoundException;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.BankRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerBankService {

    private final SellerRepository sellerRepository;
    private final BankRepository bankRepository;

    public BankRequisitesDto getRequisites(String email) {
        Seller seller = getSellerFromDatabase(email);
        if (seller.getRequisites() == null) throw new UserNotFoundException("Банковские реквизиты отсутствуют");
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
        if (seller.getRequisites() == null) throw new UserNotFoundException("Банковские реквизиты отсутствуют");
        bankRepository.delete(seller.getRequisites());
        seller.setRequisites(null);
        sellerRepository.save(seller);
    }

    private Seller getSellerFromDatabase(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует"));
    }
}
