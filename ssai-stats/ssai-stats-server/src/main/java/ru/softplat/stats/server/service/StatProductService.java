package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.server.model.StatProduct;
import ru.softplat.stats.server.repository.StatProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StatProductService {

    private final StatProductRepository statProductRepository;
    private final StatSellerService statSellerService;

    public void createStatProduct(StatProduct statProduct) {
        statSellerService.createStatSeller(statProduct.getSeller());
        statProductRepository.save(statProduct);
    }
}
