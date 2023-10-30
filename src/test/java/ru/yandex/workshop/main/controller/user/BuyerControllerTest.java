package ru.yandex.workshop.main.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.dto.buyer.BuyerDto;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.exception.ClientErrorException;
import ru.yandex.workshop.main.exception.UserNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static BuyerDto buyerDto;

    @BeforeEach
    void init() {
        buyerDto = BuyerDto.builder()
                .firstName("Joe")
                .lastName("Doe")
                .email("joedoe@email.com")
                .telephone("+70123456789")
                .build();
    }

    @Test
    @SneakyThrows
    void addNewBuyer_whenCorrect_thenReturnNewBuyer() {
        BuyerResponseDto response = createBuyer(buyerDto);
        assertEquals(1, response.getId());
        assertEquals(buyerDto.getFirstName(), response.getFirstName());
        assertEquals(buyerDto.getLastName(), response.getLastName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getTelephone(), response.getTelephone());
    }

    @Test
    @SneakyThrows
    void addNewBuyer_whenEmailNotUnique_thenThrowClientErrorException() {
        createBuyer(buyerDto);
        BuyerDto newBuyerDto = BuyerDto.builder()
                .firstName("Foo")
                .lastName("Bar")
                .telephone("+70123456789")
                .email("joedoe@email.com")
                .build();

        mockMvc.perform(post("/buyer")
                        .content(objectMapper.writeValueAsString(newBuyerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ClientErrorException));
    }

    @Test
    @SneakyThrows
    void getBuyerById_whenIdCorrect_thenReturnBuyer() {
        createBuyer(buyerDto);
        final long id = 1;

        BuyerResponseDto response = getBuyer(id);
        assertEquals(buyerDto.getFirstName(), response.getFirstName());
        assertEquals(buyerDto.getLastName(), response.getLastName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getTelephone(), response.getTelephone());
    }

    @Test
    @SneakyThrows
    void getBuyerById_whenIdIsNotCorrect_thenThrowException() {
        createBuyer(buyerDto);
        final long id = 2;

        mockMvc.perform(get("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof UserNotFoundException));
    }

    @Test
    @SneakyThrows
    void updateBuyerById_whenIdIsCorrect_thenUpdateBuyer() {
        createBuyer(buyerDto);
        BuyerDto updateDto = BuyerDto.builder()
                .firstName("Foo")
                .lastName("Bar")
                .telephone("+70123456789")
                .email("foobar@email.com")
                .build();
        final long id = 1;

        BuyerResponseDto response = updateBuyer(updateDto, id);
        assertEquals(1, response.getId());
        assertEquals(updateDto.getFirstName(), response.getFirstName());
        assertEquals(updateDto.getLastName(), response.getLastName());
        assertEquals(updateDto.getEmail(), response.getEmail());
        assertEquals(updateDto.getTelephone(), response.getTelephone());
    }

    @Test
    @SneakyThrows
    void updateBuyerById_whenIdIsNotCorrect_thenThrowUserNotFoundException() {
        createBuyer(buyerDto);
        BuyerDto updateDto = BuyerDto.builder()
                .firstName("Foo")
                .lastName("Bar")
                .telephone("+70123456789")
                .email("foobar@email.com")
                .build();
        final long id = 2;

        mockMvc.perform(patch("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof UserNotFoundException));
    }

    BuyerResponseDto createBuyer(BuyerDto buyerDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(buyerDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(buyerDto.getLastName()))
                .andExpect(jsonPath("$.email").value(buyerDto.getEmail()))
                .andExpect(jsonPath("$.telephone").value(buyerDto.getTelephone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }

    BuyerResponseDto getBuyer(long buyerId) throws Exception {
        MvcResult result = mockMvc.perform(get("/buyer/{buyerId}", buyerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }

    BuyerResponseDto updateBuyer(BuyerDto updateDto, long buyerId) throws Exception {
        MvcResult result = mockMvc.perform(patch("/buyer/{buyerId}", buyerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(updateDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateDto.getLastName()))
                .andExpect(jsonPath("$.email").value(updateDto.getEmail()))
                .andExpect(jsonPath("$.telephone").value(updateDto.getTelephone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }

}