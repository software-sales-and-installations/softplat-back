package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.buyer.BuyerDto;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.dto.buyer.BuyerMapper;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.main.message.ExceptionMessage;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BuyerService {

    private final BuyerRepository buyerRepository;

    @Transactional
    public BuyerResponseDto addNewBuyer(BuyerDto buyerDto) {
        Buyer request = BuyerMapper.INSTANCE.buyerDtoToBuyer(buyerDto);
        checkIfUserExistsByEmail(request.getEmail());
        request.setRegistrationTime(LocalDateTime.now());
        Buyer response = buyerRepository.save(request);
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(response);
    }

    @Transactional(readOnly = true)
    public BuyerResponseDto getBuyer(long buyerId) {
        Buyer response = getBuyerOrThrowExceptionIfNotFound(buyerId);
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(response);
    }

    @Transactional
    public BuyerResponseDto updateBuyer(long buyerId, BuyerDto updateDto) {
        Buyer oldBuyer = getBuyerOrThrowExceptionIfNotFound(buyerId);
        Buyer updatedBuyer = updateBuyer(oldBuyer, updateDto);
        buyerRepository.save(updatedBuyer);
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(updatedBuyer);
    }

    private void checkIfUserExistsByEmail(String email) {
        if (buyerRepository.existsBuyerByEmail(email)) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + email);
        }
    }

    private Buyer getBuyerOrThrowExceptionIfNotFound(long id) {
        return buyerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
        );
    }

    private Buyer updateBuyer(Buyer oldBuyer, BuyerDto updateDto) {
        if (updateDto.getFirstName() != null) {
            oldBuyer.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            oldBuyer.setLastName(updateDto.getLastName());
        }
        if (updateDto.getEmail() != null) {
            checkIfUserExistsByEmail(updateDto.getEmail());
            oldBuyer.setEmail(updateDto.getEmail());
        }
        if (updateDto.getTelephone() != null) {
            oldBuyer.setTelephone(updateDto.getTelephone());
        }
        return oldBuyer;
    }

}
