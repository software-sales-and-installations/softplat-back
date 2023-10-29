package ru.yandex.workshop.main.service.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.SellerRepository;

@Service
@Transactional(readOnly = true)
public class SellerBankService {
    private final SellerRepository sellerRepository;

    @Autowired
    public SellerBankService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public BankRequisitesDto getRequisites(String email) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("Такого пользователя не существует"));
        if (seller.getRequisites() == null) throw new NullPointerException("Банковские реквизиты отсутствуют");
        return new BankRequisitesDto(seller.getRequisites().getAccount());
    }

    @Transactional
    public BankRequisitesDto updateRequisites(String email, String requisites) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("Такого пользователя не существует"));
        seller.setRequisites(new BankRequisites(null, requisites));
        return new BankRequisitesDto(seller.getRequisites().getAccount());
    }

    @Transactional
    public void deleteRequisites(String email) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("Такого пользователя не существует"));
        if (seller.getRequisites() == null) throw new NullPointerException("Банковские реквизиты отсутствуют");
        seller.setRequisites(null);
        sellerRepository.save(seller);
    }
}
