package ru.softplat.main.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.AbstractControllerTest;
import ru.yandex.workshop.main.dto.user.BuyerUpdateDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.security.dto.UserCreateDto;
import ru.yandex.workshop.security.model.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerControllerTest extends AbstractControllerTest {

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
    void addNewBuyer_shouldReturnNewBuyer_whenRequestDtoIsValid() {
        createUser(userDto);

        BuyerResponseDto actualBuyer = getBuyerResponseDto(1L);

        assertEquals(1, actualBuyer.getId());
        assertEquals(userDto.getName(), actualBuyer.getName());
        assertEquals(userDto.getEmail(), actualBuyer.getEmail());
        assertEquals(userDto.getPhone(), actualBuyer.getPhone());
    }

    @Test
    @SneakyThrows
    void addNewBuyer_shouldThrowDuplicateException_whenEmailNotUnique() {
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
    void getBuyerById_shouldReturnBuyer_whenIdCorrect() {
        createUser(userDto);
        final long id = 1;

        BuyerResponseDto actual = getBuyerResponseDto(id);
        assertEquals(userDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"buyer:write"})
    void getBuyerById_shouldThrowException_whenIdIsNotCorrect() {
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
    void updateBuyerById_shouldUpdateBuyer_whenRequestDtoIsValid() {
        createUser(userDto);
        BuyerUpdateDto updateDto = BuyerUpdateDto.builder()
                .name("Foo")
                .build();

        BuyerResponseDto actual = updateBuyer(updateDto);

        assertEquals(1, actual.getId());
        assertEquals(updateDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "foobar@email.com",authorities = {"buyer:write"})
    void updateBuyerById_shouldThrowUserNotFoundException_whenIdIsNotCorrect() {
        createUser(userDto);
        BuyerUpdateDto updateDto = BuyerUpdateDto.builder()
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

    BuyerResponseDto updateBuyer(BuyerUpdateDto updateDto) throws Exception {
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

