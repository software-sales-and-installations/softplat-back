package ru.yandex.workshop.main.service.seller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.user.SellerDto;
import ru.yandex.workshop.main.dto.user.mapper.SellerMapper;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.service.ChangeService;

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
    private ChangeService changeService;


    private UserDto userDto;
    private Seller seller;
    private SellerDto sellerDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().email("email@email.com").build();
        seller = SellerMapper.INSTANCE.userDtoToSeller(userDto);
        sellerDto = SellerDto.builder().email("update@update.com").build();
    }

    @Test
    void addSeller_whenSellerExists_throwDuplicateException() {
        when(sellerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(seller));

        assertThrows(DuplicateException.class,
                () -> sellerService.addSeller(userDto));
    }

    @Test
    @SneakyThrows
    void addSeller_whenPhoneIsNullOrNotEntered_throwValidationException() {
        assertThrows(ValidationException.class,
                () -> sellerService.addSeller(userDto));

        userDto.setPhone("");
        assertThrows(ValidationException.class,
                () -> sellerService.addSeller(userDto));
    }

    @Test
    @SneakyThrows
    void addSeller_whenDescriptionIsNullOrNotEntered_throwValidationException() {
        userDto.setPhone("01234567890");
        assertThrows(ValidationException.class,
                () -> sellerService.addSeller(userDto));

        userDto.setDescription("");
        assertThrows(ValidationException.class,
                () -> sellerService.addSeller(userDto));
    }

    @Test
    void addSeller_whenValid_thenReturnSeller() {
        userDto.setPhone("01234567890");
        userDto.setDescription("description");

        seller.setPhone(userDto.getPhone());
        seller.setDescription(userDto.getDescription());

        when(sellerRepository.save(any())).thenReturn(seller);

        Seller expect = SellerMapper.INSTANCE.userDtoToSeller(userDto);
        Seller actual = sellerRepository.save(seller);
        assertEquals(expect.getEmail(), actual.getEmail());
        assertEquals(expect.getPhone(), actual.getPhone());
        assertEquals(expect.getDescription(), actual.getDescription());
    }

    @Test
    void updateSeller_whenEmailNotValid_throwEntityNotFoundException() {
        when(sellerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> sellerService.updateSeller(
                        sellerDto.getEmail(),
                        sellerDto
                ));
    }

    @Test
    void updateSeller_whenUpdatedEmailExists_throwDuplicateException() {
        sellerDto.setEmail(userDto.getEmail());

        when(sellerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(seller));

        assertThrows(DuplicateException.class,
                () -> sellerService.updateSeller(
                        sellerDto.getEmail(),
                        sellerDto
                ));
    }

    @Test
    void updateSeller_whenValid_thenReturnSellerResponseDto() {
        SellerDto updateDto = SellerDto.builder()
                .name("name")
                .description("description")
                .build();

        seller.setName(updateDto.getName());
        seller.setDescription(updateDto.getDescription());
        seller.setEmail(updateDto.getEmail());
        seller.setPhone(updateDto.getPhone());

        verify(changeService, never()).changeEmail(anyString(), anyString());

        when(sellerRepository.save(any())).thenReturn(seller);
        when(sellerRepository.findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));

        SellerResponseDto expect = SellerMapper.INSTANCE.sellerToSellerResponseDto(seller);
        SellerResponseDto actual = sellerService.updateSeller(seller.getEmail(), updateDto);

        assertEquals(expect, actual);
    }

    @Test
    void getAllSellers_whenCalled_thenReturnAllSellers() {
        int from = 0;
        int size = 1;

        lenient().when(sellerRepository.findAll(PageRequestOverride.of(from, size)))
                .thenReturn(new PageImpl<>(List.of(seller)));

        SellerResponseDto dto = SellerMapper.INSTANCE.sellerToSellerResponseDto(seller);
        List<SellerResponseDto> expect = List.of(dto);
        List<SellerResponseDto> actual = sellerService.getAllSellers(from, size);
        assertEquals(expect, actual);
    }

    @Test
    void getSellerDto_whenIdNotValid_throwEntityNotFoundException() {
        long sellerId = 1L;
        seller.setId(sellerId);

        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> sellerService.getSellerDto(seller.getId()));
    }

    @Test
    void getSellerDto_whenIdValid_thenReturnSellerResponseDto() {
        long sellerId = 1L;
        seller.setId(sellerId);

        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.of(seller));

        SellerResponseDto expect = SellerMapper.INSTANCE.sellerToSellerResponseDto(seller);
        SellerResponseDto actual = sellerService.getSellerDto(seller.getId());
        assertEquals(expect, actual);
    }
}