package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.server.model.StatBuyer;
import ru.softplat.stats.server.repository.StatBuyerRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StatBuyerService {

    private final StatBuyerRepository statBuyerRepository;

    public void createStatBuyer(StatBuyer buyer) {
        statBuyerRepository.save(buyer);
    }

}
