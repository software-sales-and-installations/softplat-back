package ru.softplat.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.configuration.PageRequestOverride;
import ru.softplat.exception.DuplicateException;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.model.buyer.Buyer;
import ru.softplat.repository.buyer.BuyerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final UserDetailsChangeService userDetailsChangeService;

    @Transactional(readOnly = true)
    public List<Buyer> getAllBuyers(int from, int size) {
        return buyerRepository.findAll(PageRequestOverride.of(from, size)).stream()
                .collect(Collectors.toList());
    }

    public Buyer addBuyer(Buyer buyer) {
        if (checkIfUserExistsByEmail(buyer.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + buyer.getEmail());

        buyer.setRegistrationTime(LocalDateTime.now());

        return buyerRepository.save(buyer);
    }

    public Buyer updateBuyer(String email, Buyer updateRequest) {
        Buyer oldBuyer = getBuyerByEmail(email);

        if (updateRequest.getName() != null) {
            oldBuyer.setName(updateRequest.getName());
        }
        if (updateRequest.getEmail() != null) {
            if (checkIfUserExistsByEmail(updateRequest.getEmail()))
                throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + updateRequest.getEmail());
            userDetailsChangeService.changeEmail(oldBuyer.getEmail(), updateRequest.getEmail());
            oldBuyer.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhone() != null) {
            oldBuyer.setPhone(updateRequest.getPhone());
        }

        return buyerRepository.save(oldBuyer);
    }

    @Transactional(readOnly = true)
    public Buyer getBuyer(Long userId) {
        return buyerRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(userId), Buyer.class)
                ));
    }

    @Transactional(readOnly = true)
    public Buyer getBuyerByEmail(String email) {
        return buyerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(email, Buyer.class)
                ));
    }

    @Transactional(readOnly = true)
    public boolean checkIfUserExistsByEmail(String email) {
        return buyerRepository.findByEmail(email).isPresent();
    }
}
