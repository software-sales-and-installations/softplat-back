package ru.softplat.main.service.seller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.security.service.UserDetailsChangeService;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @InjectMocks
    private SellerService sellerService;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private UserDetailsChangeService changeService;

    private Seller seller;
    private Seller updateSeller;

    @BeforeEach
    void setUp() {
        seller = Seller.builder().email("email@email.com").build();
        updateSeller = Seller.builder().email("update@update.com").build();
    }

    @Test
    void addSeller_shouldThrowDuplicateException_whenSellerAlreadyExists() {
        when(sellerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(seller));

        assertThrows(DuplicateException.class,
                () -> sellerService.addSeller(seller));
    }

    @Disabled
    @Test
    @SneakyThrows
    void addSeller_shouldThrowValidationException_whenPhoneIsNullOrNotEntered() {
        assertThrows(ValidationException.class,
                () -> sellerService.addSeller(seller));
    }

    @Disabled
    @Test
    @SneakyThrows
    void addSeller_shouldThrowValidationException_whenDescriptionIsNullOrNotEntered() {
        seller.setPhone("01234567890");

        assertThrows(ValidationException.class,
                () -> sellerService.addSeller(seller));
    }

    @Test
    void addSeller_shouldReturnSeller_whenRequestDtoIsValid() {
        seller.setPhone("01234567890");
        Seller expect = seller;

        when(sellerRepository.save(any())).thenReturn(seller);
        Seller actual = sellerService.addSeller(seller);

        assertEquals(expect.getEmail(), actual.getEmail());
        assertEquals(expect.getPhone(), actual.getPhone());
    }

    @Test
    void updateSeller_shouldThrowEntityNotFoundException_whenEmailNotValid() {
        when(sellerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> sellerService.updateSeller(
                        seller.getEmail(),
                        seller
                ));
    }

    @Test
    void updateSeller_shouldThrowDuplicateException_whenUpdatedEmailExists() {
        seller.setEmail(updateSeller.getEmail());

        when(sellerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(seller));

        assertThrows(DuplicateException.class,
                () -> sellerService.updateSeller(
                        seller.getEmail(),
                        seller
                ));
    }

    @Test
    void updateSeller_shouldReturnSellerResponseDto_whenValid() {
        seller.setName(updateSeller.getName());
        seller.setPhone(updateSeller.getPhone());
        Seller expect = seller;

        when(sellerRepository.save(any())).thenReturn(seller);
        when(sellerRepository.findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));

        verify(changeService, never()).changeEmail(anyString(), anyString());
        Seller actual = sellerService.updateSeller(seller.getEmail(), updateSeller);

        assertEquals(expect, actual);
    }

    @Test
    void getAllSellers_shouldReturnAllSellers() {
        int from = 0;
        int size = 1;
        List<Seller> expect = List.of(seller);

        when(sellerRepository.findAll(PageRequestOverride.of(from, size)))
                .thenReturn(new PageImpl<>(List.of(seller)));
        List<Seller> actual = sellerService.getAllSellers(from, size);

        assertEquals(expect, actual);
    }

    @Test
    void getSellerDto_shouldThrowEntityNotFoundException_whenIdNotValid_() {
        long sellerId = 1L;
        seller.setId(sellerId);

        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> sellerService.getSeller(seller.getId()));
    }

    @Test
    void getSellerDto_shouldReturnSellerResponseDto_whenIdValid() {
        long sellerId = 1L;
        seller.setId(sellerId);
        Seller expect = seller;

        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.of(seller));
        Seller actual = sellerService.getSeller(seller.getId());

        assertEquals(expect, actual);
    }
}