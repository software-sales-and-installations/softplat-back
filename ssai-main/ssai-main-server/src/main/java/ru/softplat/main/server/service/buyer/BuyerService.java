package ru.softplat.main.server.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.repository.buyer.BuyerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerService {
    private final BuyerRepository buyerRepository;

    @Transactional(readOnly = true)
    public List<Buyer> getAllBuyers(int from, int size) {
        return buyerRepository.findAll(PageRequestOverride.of(from, size)).stream()
                .collect(Collectors.toList());
    }

    public Buyer addBuyer(Buyer buyer) {
        buyer.setRegistrationTime(LocalDateTime.now());

        return buyerRepository.save(buyer);
    }

    public Buyer updateBuyer(long userId, Buyer updateRequest) {
        Buyer oldBuyer = getBuyer(userId);

        if (updateRequest.getName() != null) {
            oldBuyer.setName(updateRequest.getName());
        }
        /*if (updateRequest.getEmail() != null) {
            if (checkIfBuyerExistsByEmail(updateRequest.getEmail()))
                throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + updateRequest.getEmail());
            userDetailsChangeService.changeEmail(oldBuyer.getEmail(), updateRequest.getEmail());
            oldBuyer.setEmail(updateRequest.getEmail());
        }*/
        if (updateRequest.getPhone() != null) {
            oldBuyer.setPhone(updateRequest.getPhone());
        }

        return buyerRepository.save(oldBuyer);
    }

    @Transactional(readOnly = true)
    public Buyer getBuyer(Long userId) {
        return buyerRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, Buyer.class)
                ));
    }

    public void deleteBuyer(long userId){
        getBuyer(userId);

        buyerRepository.deleteById(userId);
    }
}
