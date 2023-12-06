package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.server.model.StatSeller;
import ru.softplat.stats.server.repository.StatSellerRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StatSellerService {

    private final StatSellerRepository statSellerRepository;

    public void createStatSeller(StatSeller statSeller) {
        statSellerRepository.save(statSeller);
    }
}
