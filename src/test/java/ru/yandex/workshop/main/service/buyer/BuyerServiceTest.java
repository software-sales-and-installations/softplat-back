package ru.yandex.workshop.main.service.buyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.security.service.UserDetailsChangeService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyerServiceTest {

    @InjectMocks
    private BuyerService buyerService;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private UserDetailsChangeService changeService;

    private Buyer buyer;
    private Buyer updateBuyer;

    @BeforeEach
    void setUp() {
        buyer = Buyer.builder().email("email@email.com").build();
        updateBuyer = Buyer.builder().email("update@update.com").build();
    }

    @Test
    void addBuyer_whenBuyerExists_throwDuplicateException() {
        when(buyerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(buyer));

        assertThrows(DuplicateException.class,
                () -> buyerService.addBuyer(buyer));
    }


    @Test
    void addBuyer_whenValid_thenReturnBuyer() {
        buyer.setPhone("1234567890");
        buyer.setRegistrationTime(LocalDateTime.now());

        when(buyerRepository.save(any())).thenReturn(buyer);

        Buyer actual = buyer;
        Buyer expect = buyerRepository.save(buyer);
        assertEquals(expect, actual);
    }

    @Test
    void updateBuyer_whenEmailNotValid_throwEntityNotFoundException() {
        when(buyerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> buyerService.updateBuyer(
                        buyer.getEmail(),
                        updateBuyer
                ));
    }

    @Test
    void updateBuyer_whenUpdatedEmailExists_throwDuplicateException() {
        buyer.setEmail("email@email.com");

        when(buyerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(buyer));

        assertThrows(DuplicateException.class,
                () -> buyerService.updateBuyer(
                        buyer.getEmail(),
                        updateBuyer
                ));
    }

    @Test
    void updateBuyer_whenValid_thenReturnBuyerResponseDto() {
        buyer.setName(updateBuyer.getName());
        buyer.setPhone(updateBuyer.getPhone());

        verify(changeService, never()).changeEmail(anyString(), anyString());

        when(buyerRepository.save(any())).thenReturn(buyer);
        when(buyerRepository.findByEmail(buyer.getEmail()))
                .thenReturn(Optional.of(buyer));

        Buyer expect = buyer;
        Buyer actual = buyerService.updateBuyer(buyer.getEmail(), updateBuyer);

        assertEquals(expect, actual);
    }

    @Test
    void getBuyer_whenIdNotValid_throwEntityNotFoundException() {
        when(buyerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> buyerService.getBuyer(1L));
    }

    @Test
    void getBuyer_whenIdValid_thenReturnBuyerResponseDto() {
        when(buyerRepository.findById(anyLong()))
                .thenReturn(Optional.of(buyer));

        Buyer expect = buyer;
        Buyer actual = buyerService.getBuyer(1L);
        assertEquals(expect, actual);
    }
}