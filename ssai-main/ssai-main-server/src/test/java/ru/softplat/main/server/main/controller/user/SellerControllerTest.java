package ru.softplat.main.server.main.controller.user;

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
import ru.yandex.workshop.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.yandex.workshop.main.dto.user.SellerUpdateDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
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
class SellerControllerTest extends AbstractControllerTest {

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
                .role(Role.SELLER)
                .build();
    }

    @Test
    @SneakyThrows
    void addNewSeller_shouldReturnNewSeller_whenRequestDtoIsValid() {
        createUser(userDto);

        SellerResponseDto actual = getSellerResponseDto(1L);

        assertEquals(userDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    void addNewSeller_shouldThrowDuplicateException_whenEmailNotUnique() {
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
    void getSellerById_shouldReturnSeller_whenIdCorrect() {
        createUser(userDto);
        final long id = 1;

        SellerResponseDto actual = getSellerResponseDto(id);

        assertEquals(userDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    void getSellerByEmail_shouldThrowException_whenEmailIsNotCorrect() {
        createUser(userDto);
        final long id = 2;

        mockMvc.perform(get("/seller/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write"})
    void updateSellerByEmail_shouldReturnUpdatedSeller_whenEmailIsCorrect() {
        createUser(userDto);
        SellerUpdateDto updateDto = SellerUpdateDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();

        SellerResponseDto actual = updateSeller(updateDto);

        assertEquals(updateDto.getName(), actual.getName());
        assertEquals(updateDto.getEmail(), actual.getEmail());
        assertEquals(updateDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "foobar@email.com", authorities = {"seller:write"})
    void updateSellerByEmail_shouldThrowUserNotFoundException_whenEmailIsNotCorrect() {
        createUser(userDto);
        SellerUpdateDto updateDto = SellerUpdateDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();

        mockMvc.perform(patch("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write"})
    void addRequisites_shouldReturnRequisites_whenRequestDtoIsValid() {
        createUser(userDto);
        BankRequisitesCreateUpdateDto requisitesDto = new BankRequisitesCreateUpdateDto("1234567891234567");

        BankRequisitesCreateUpdateDto actual = addRequisites(requisitesDto);

        assertEquals(requisitesDto.getAccount(), actual.getAccount());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write"})
    void updateRequisites_shouldReturnUpdatedRequisites_whenRequestDtoIsValid() {
        createUser(userDto);
        BankRequisitesCreateUpdateDto requisitesDto = new BankRequisitesCreateUpdateDto("1234567891234567");
        addRequisites(requisitesDto);
        BankRequisitesCreateUpdateDto newRequisitesDto = new BankRequisitesCreateUpdateDto("1111111111111111");

        mockMvc.perform(patch("/seller/bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequisitesDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account").value(newRequisitesDto.getAccount()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write"})
    void deleteRequisites_shouldExecuteAndReturnStatusNoContent() {
        createUser(userDto);
        BankRequisitesCreateUpdateDto requisitesDto = new BankRequisitesCreateUpdateDto("1234567891234567");
        addRequisites(requisitesDto);

        mockMvc.perform(delete("/seller/bank")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    SellerResponseDto updateSeller(SellerUpdateDto updateDto) {
        MvcResult result = mockMvc.perform(patch("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.email").value(updateDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(updateDto.getPhone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerResponseDto.class
        );
    }

    @SneakyThrows
    BankRequisitesCreateUpdateDto addRequisites(BankRequisitesCreateUpdateDto requisitesDto) {
        MvcResult result = mockMvc.perform(patch("/seller/bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requisitesDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account").value(requisitesDto.getAccount()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BankRequisitesCreateUpdateDto.class
        );
    }
}