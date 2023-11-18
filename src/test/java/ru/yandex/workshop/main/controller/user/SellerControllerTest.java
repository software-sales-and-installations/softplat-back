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
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.dto.user.SellerDto;
import ru.yandex.workshop.security.dto.UserCreateDto;
import ru.yandex.workshop.security.model.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SellerControllerTest extends CrudOperations {

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
    void addNewSeller_whenCorrect_thenReturnNewSeller() {
        createUser(userDto);
        SellerResponseDto response = getSellerResponseDto(1L);
        assertEquals(userDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals(userDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void addNewSeller_whenEmailNotUnique_thenThrowDuplicateException() {
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
    void getSellerById_whenCorrect_thenReturnSeller() {
        createUser(userDto);
        final long id = 1;

        SellerResponseDto response = getSellerResponseDto(id);
        assertEquals(userDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals(userDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void getSellerByEmail_whenEmailIsNotCorrect_thenThrowException() {
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
    void updateSellerByEmail_whenEmailIsCorrect_thenUpdateSeller() {
        createUser(userDto);
        SellerDto updateDto = SellerDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();

        SellerResponseDto response = updateSeller(updateDto);
        assertEquals(updateDto.getName(), response.getName());
        assertEquals(updateDto.getEmail(), response.getEmail());
        assertEquals(updateDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "foobar@email.com", authorities = {"seller:write"})
    void updateSellerByEmail_whenEmailIsNotCorrect_thenThrowUserNotFoundException() {
        createUser(userDto);
        SellerDto updateDto = SellerDto.builder()
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
    void addRequisites_whenOk_returnRequisites() {
        createUser(userDto);
        BankRequisitesDto requisitesDto = new BankRequisitesDto("1234567891234567");

        BankRequisitesDto response = addRequisites(requisitesDto);
        assertEquals(requisitesDto.getAccount(), response.getAccount());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write"})
    void updateRequisites_whenOk_returnRequisites() {
        createUser(userDto);
        BankRequisitesDto requisitesDto = new BankRequisitesDto("1234567891234567");
        addRequisites(requisitesDto);
        BankRequisitesDto newRequisitesDto = new BankRequisitesDto("1111111111111111");

        mockMvc.perform(patch("/seller/bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequisitesDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account").value(newRequisitesDto.getAccount()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write", "admin:write"})
    void deleteRequisites_whenOk() {
        createUser(userDto);
        BankRequisitesDto requisitesDto = new BankRequisitesDto("1234567891234567");
        addRequisites(requisitesDto);

        mockMvc.perform(delete("/seller/bank")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    SellerResponseDto updateSeller(SellerDto updateDto) {
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

    BankRequisitesDto addRequisites(BankRequisitesDto requisitesDto) throws Exception {
        MvcResult result = mockMvc.perform(patch("/seller/bank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requisitesDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account").value(requisitesDto.getAccount()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BankRequisitesDto.class
        );
    }
}