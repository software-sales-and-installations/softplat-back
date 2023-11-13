//package ru.yandex.workshop.main.service.buyer;
//
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.yandex.workshop.main.dto.user.BuyerDto;
//import ru.yandex.workshop.main.mapper.BuyerMapper;
//import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
//import ru.yandex.workshop.main.exception.DuplicateException;
//import ru.yandex.workshop.main.exception.EntityNotFoundException;
//import ru.yandex.workshop.main.model.buyer.Buyer;
//import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
//import ru.yandex.workshop.security.dto.UserDto;
//import ru.yandex.workshop.security.service.ChangeService;
//
//import javax.xml.bind.ValidationException;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BuyerServiceTest {
//
//    @InjectMocks
//    private BuyerService buyerService;
//    @Mock
//    private BuyerRepository buyerRepository;
//    @Mock
//    private ChangeService changeService;
//
//    private UserDto userDto;
//    private Buyer buyer;
//
//    private BuyerDto buyerDto;

//    @BeforeEach
//    void setUp() {
//        userDto = UserDto.builder().email("email@email.com").build();
//        buyer = BuyerMapper.INSTANCE.buyerDtoToBuyer(userDto);
//        buyerDto = BuyerDto.builder().email("update@update.com").build();
//    }

//    @Test
//    void addBuyer_whenBuyerExists_throwDuplicateException() {
//        when(buyerRepository.findByEmail(anyString()))
//                .thenReturn(Optional.of(buyer));
//
//        assertThrows(DuplicateException.class,
//                () -> buyerService.addBuyer(userDto));
//    }

//    @Test
//    @SneakyThrows
//    void addBuyer_whenPhoneNotEntered_throwValidationException() {
//        assertThrows(ValidationException.class,
//                () -> buyerService.addBuyer(userDto));
//    }

//    @Test
//    void addBuyer_whenValid_thenReturnBuyer() {
//        userDto.setPhone("1234567890");
//        buyer = BuyerMapper.INSTANCE.buyerDtoToBuyer(userDto);
//        buyer.setRegistrationTime(LocalDateTime.now());
//
//        when(buyerRepository.save(any())).thenReturn(buyer);
//
//        Buyer actual = buyer;
//        Buyer expect = buyerRepository.save(buyer);
//        assertEquals(expect, actual);
//    }

//    @Test
//    void updateBuyer_whenEmailNotValid_throwEntityNotFoundException() {
//        when(buyerRepository.findByEmail(anyString()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class,
//                () -> buyerService.updateBuyer(
//                        buyerDto.getEmail(),
//                        buyerDto
//                ));
//    }

//    @Test
//    void updateBuyer_whenUpdatedEmailExists_throwDuplicateException() {
//        buyerDto.setEmail("email@email.com");
//
//        when(buyerRepository.findByEmail(anyString()))
//                .thenReturn(Optional.of(buyer));
//
//        assertThrows(DuplicateException.class,
//                () -> buyerService.updateBuyer(
//                        buyerDto.getEmail(),
//                        buyerDto
//                ));
//    }

//    @Test
//    void updateBuyer_whenValid_thenReturnBuyerResponseDto() {
//        BuyerDto updateDto = BuyerDto.builder()
//                .name("name")
//                .phone("0987654321")
//                .build();
//
//        buyer.setName(updateDto.getName());
//        buyer.setPhone(updateDto.getPhone());
//
//        verify(changeService, never()).changeEmail(anyString(), anyString());
//
//        when(buyerRepository.save(any())).thenReturn(buyer);
//        when(buyerRepository.findByEmail(buyer.getEmail()))
//                .thenReturn(Optional.of(buyer));
//
//        BuyerResponseDto expect = BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyer);
//        BuyerResponseDto actual = buyerService.updateBuyer(buyer.getEmail(), updateDto);
//
//        assertEquals(expect, actual);
//    }

//    @Test
//    void getBuyer_whenIdNotValid_throwEntityNotFoundException() {
//        when(buyerRepository.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class,
//                () -> buyerService.getBuyer(1L));
//    }

//    @Test
//    void getBuyer_whenIdValid_thenReturnBuyerResponseDto() {
//        when(buyerRepository.findById(anyLong()))
//                .thenReturn(Optional.of(buyer));
//
//        BuyerResponseDto expect = BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyer);
//        BuyerResponseDto actual = buyerService.getBuyer(1L);
//        assertEquals(expect, actual);
//    }

//}