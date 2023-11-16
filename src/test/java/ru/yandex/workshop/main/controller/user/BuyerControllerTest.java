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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.security.dto.UserCreateDto;
import ru.yandex.workshop.security.model.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private UserCreateDto userDto;

    @BeforeEach
    void init() {
        userDto = UserCreateDto.builder()
                .name("Joe")
                .email("joedoe@email.com")
                .phone("0123456789")
                .password("Password12345!")
                .confirmPassword("Password12345!")
                .role(Role.BUYER)
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"buyer:write"})
    void addNewBuyer_whenCorrect_thenReturnNewBuyer() {
        createUser(userDto);
        BuyerResponseDto response = getBuyerResponseDto(1L);
        assertEquals(1, response.getId());
        assertEquals(userDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals(userDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void addNewBuyer_whenEmailNotUnique_thenThrowDuplicateException() {
        createUser(userDto);
        UserCreateDto newUserDto = userDto;

        mockMvc.perform(post("/registration")
                        .content(objectMapper.writeValueAsString(newUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof DuplicateException));
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"buyer:write"})
    void getBuyerById_whenIdCorrect_thenReturnBuyer() {
        createUser(userDto);
        final long id = 1;

        BuyerResponseDto response = getBuyerResponseDto(id);
        assertEquals(userDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals(userDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"buyer:write"})
    void getBuyerById_whenIdIsNotCorrect_thenThrowException() {
        createUser(userDto);
        final long id = 2;

        mockMvc.perform(get("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com",authorities = {"buyer:write"})
    void updateBuyerById_whenIsCorrect_thenUpdateBuyer() {
        createUser(userDto);
        BuyerDto updateDto = BuyerDto.builder()
                .name("Foo")
                .build();

        BuyerResponseDto response = updateBuyer(updateDto);
        assertEquals(1, response.getId());
        assertEquals(updateDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals(userDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "foobar@email.com",authorities = {"buyer:write"})
    void updateBuyerById_whenIdIsNotCorrect_thenThrowUserNotFoundException() {
        createUser(userDto);
        BuyerDto updateDto = BuyerDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();

        mockMvc.perform(patch("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    BuyerResponseDto updateBuyer(BuyerDto updateDto) throws Exception {
        MvcResult result = mockMvc.perform(patch("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(userDto.getPhone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }
}

