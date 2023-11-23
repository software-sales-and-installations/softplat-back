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
    void addBuyer_shouldThrowDuplicateException_whenBuyerExists() {
        when(buyerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(buyer));

        assertThrows(DuplicateException.class,
                () -> buyerService.addBuyer(buyer));
    }


    @Test
    void addBuyer_shouldReturnBuyer_whenValid() {
        buyer.setPhone("1234567890");
        buyer.setRegistrationTime(LocalDateTime.now());
        Buyer expect = buyer;

        when(buyerRepository.save(any())).thenReturn(buyer);
        Buyer actual = buyerService.addBuyer(buyer);

        assertEquals(expect, actual);
    }

    @Test
    void updateBuyer_shouldThrowEntityNotFoundException_whenEmailNotValid() {
        when(buyerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> buyerService.updateBuyer(
                        buyer.getEmail(),
                        updateBuyer
                ));
    }

    @Test
    void updateBuyer_shouldThrowDuplicateException_whenUpdatedEmailExists() {
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
    void updateBuyer_shouldReturnBuyerResponseDto_whenValid() {
        buyer.setName(updateBuyer.getName());
        buyer.setPhone(updateBuyer.getPhone());
        Buyer expect = buyer;

        verify(changeService, never()).changeEmail(anyString(), anyString());
        when(buyerRepository.save(any())).thenReturn(buyer);
        when(buyerRepository.findByEmail(buyer.getEmail()))
                .thenReturn(Optional.of(buyer));

        Buyer actual = buyerService.updateBuyer(buyer.getEmail(), updateBuyer);

        assertEquals(expect, actual);
    }

    @Test
    void getBuyer_shouldThrowEntityNotFoundException_whenIdNotValid() {
        when(buyerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> buyerService.getBuyer(1L));
    }

    @Test
    void getBuyer_shouldReturnBuyerResponseDto_whenIdValid() {
        Buyer expect = buyer;

        when(buyerRepository.findById(anyLong()))
                .thenReturn(Optional.of(buyer));
        Buyer actual = buyerService.getBuyer(1L);

        assertEquals(expect, actual);
    }
}