package ru.yandex.workshop.main.controller.user;
/*
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
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .name("Joe")
                .email("joedoe@email.com")
                .phone("0123456789")
                .build();
    }

    @Test
    @SneakyThrows
    void addNewBuyer_whenCorrect_thenReturnNewBuyer() {
        BuyerResponseDto response = createBuyer(buyerDto);
        assertEquals(1, response.getId());
        assertEquals(buyerDto.getName(), response.getName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void addNewBuyer_whenEmailNotUnique_thenThrowDuplicateException() {
        createBuyer(buyerDto);
        BuyerDto newBuyerDto = BuyerDto.builder()
                .name("Foo")
                .phone("0123456789")
                .email("joedoe@email.com")
                .build();

        mockMvc.perform(post("/buyer")
                        .content(objectMapper.writeValueAsString(newBuyerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof DuplicateException));
    }

    @Test
    @SneakyThrows
    void getBuyerById_whenIdCorrect_thenReturnBuyer() {
        createBuyer(buyerDto);
        final long id = 1;

        BuyerResponseDto response = getBuyer(id);
        assertEquals(buyerDto.getName(), response.getName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void getBuyerById_whenIdIsNotCorrect_thenThrowException() {
        createBuyer(buyerDto);
        final long id = 2;

        mockMvc.perform(get("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    void updateBuyerById_whenIsCorrect_thenUpdateBuyer() {
        createBuyer(buyerDto);
        BuyerDto updateDto = BuyerDto.builder()
                .name("Foo")
                .build();
        final long id = 1;

        BuyerResponseDto response = updateBuyer(updateDto, id);
        assertEquals(1, response.getId());
        assertEquals(updateDto.getName(), response.getName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void updateBuyerById_whenIdIsNotCorrect_thenThrowUserNotFoundException() {
        createBuyer(buyerDto);
        BuyerDto updateDto = BuyerDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();
        final long id = 2;

        mockMvc.perform(patch("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    BuyerResponseDto createBuyer(BuyerDto buyerDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(buyerDto.getName()))
                .andExpect(jsonPath("$.email").value(buyerDto.getEmail()))
                .andExpect(jsonPath("$.telephone").value(buyerDto.getPhone()))
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
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.email").value(buyerDto.getEmail()))
                .andExpect(jsonPath("$.telephone").value(buyerDto.getPhone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }

}
*/
