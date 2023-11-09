package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.mapper.BuyerMapper;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.service.ChangeService;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final ChangeService changeService;

    @Transactional
    public void addBuyer(UserDto userDto) throws ValidationException {
        if (checkIfUserExistsByEmail(userDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + userDto.getEmail());

        if (userDto.getPhone() == null)
            throw new ValidationException("Необходимо указать номер телефона. Телефонный номер должен начинаться с +7, затем - 10 цифр.");

        Buyer buyer = BuyerMapper.INSTANCE.buyerDtoToBuyer(userDto);
        buyer.setRegistrationTime(LocalDateTime.now());

        buyerRepository.save(buyer);
    }

    @Transactional
    public BuyerResponseDto updateBuyer(String email, BuyerDto updateDto) {
        Buyer oldBuyer = getSecurityBuyer(email);

        if (updateDto.getName() != null) {
            oldBuyer.setName(updateDto.getName());
        }
        if (updateDto.getEmail() != null) {
            if (checkIfUserExistsByEmail(updateDto.getEmail()))
                throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label + updateDto.getEmail());
            changeService.changeEmail(oldBuyer.getEmail(), updateDto.getEmail());
            oldBuyer.setEmail(updateDto.getEmail());
        }
        if (updateDto.getPhone() != null) {
            oldBuyer.setPhone(updateDto.getPhone());
        }

        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyerRepository.save(oldBuyer));
    }

    public BuyerResponseDto getBuyer(Long userId) {
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    private Buyer getSecurityBuyer(String email) {
        return buyerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private boolean checkIfUserExistsByEmail(String email) {
        return buyerRepository.findByEmail(email).isPresent();
    }
}
