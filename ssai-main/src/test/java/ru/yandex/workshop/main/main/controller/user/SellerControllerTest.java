package ru.yandex.workshop.main.main.controller.user;

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
import ru.yandex.workshop.main.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.main.dto.seller.SellerForResponse;
import ru.yandex.workshop.main.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.main.exception.DuplicateException;
import ru.yandex.workshop.main.main.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static SellerDto sellerDto;

    @BeforeEach
    void init() {
        sellerDto = SellerDto.builder()
                .name("Joe")
                .email("joedoe@email.com")
                .phone("0123456789")
                .build();
    }

    @Test
    @SneakyThrows
    void addNewSeller_whenCorrect_thenReturnNewSeller() {
        SellerForResponse response = createSeller(sellerDto);
        assertEquals(sellerDto.getName(), response.getName());
        assertEquals(sellerDto.getEmail(), response.getEmail());
        assertEquals(sellerDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void addNewSeller_whenEmailNotUnique_thenThrowDuplicateException() {
        createSeller(sellerDto);
        SellerDto newSellerDto = SellerDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("joedoe@email.com")
                .build();

        mockMvc.perform(post("/seller/registration")
                        .content(objectMapper.writeValueAsString(newSellerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof DuplicateException));
    }

    @Test
    @SneakyThrows
    void getSellerByEmail_whenCorrect_thenReturnSeller() {
        createSeller(sellerDto);
        String email = "joedoe@email.com";

        SellerForResponse response = getSeller(email);
        assertEquals(sellerDto.getName(), response.getName());
        assertEquals(sellerDto.getEmail(), response.getEmail());
        assertEquals(sellerDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void getSellerByEmail_whenEmailIsNotCorrect_thenThrowException() {
        createSeller(sellerDto);
        String email = "joe@email.com";

        mockMvc.perform(get("/seller/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    void updateSellerByEmail_whenEmailIsCorrect_thenUpdateSeller() {
        createSeller(sellerDto);
        SellerForUpdate updateDto = SellerForUpdate.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();
        String email = "joedoe@email.com";

        SellerForResponse response = updateSeller(email, updateDto);
        assertEquals(updateDto.getName(), response.getName());
        assertEquals(updateDto.getEmail(), response.getEmail());
        assertEquals(updateDto.getPhone(), response.getPhone());
    }

    @Test
    @SneakyThrows
    void updateSellerByEmail_whenEmailIsNotCorrect_thenThrowUserNotFoundException() {
        createSeller(sellerDto);
        SellerDto updateDto = SellerDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();
        String email = "joe@email.com";

        mockMvc.perform(patch("/seller/account/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    void addRequisites_whenOk_returnRequisites() {
        createSeller(sellerDto);
        BankRequisitesDto requisitesDto = new BankRequisitesDto("1234567891234567");
        String email = "joedoe@email.com";

        BankRequisitesDto response = addRequisites(email, requisitesDto);
        assertEquals(requisitesDto.getAccount(), response.getAccount());
    }

    @Test
    @SneakyThrows
    void updateRequisites_whenOk_returnRequisites() {
        createSeller(sellerDto);
        BankRequisitesDto requisitesDto = new BankRequisitesDto("1234567891234567");
        String email = "joedoe@email.com";
        addRequisites(email, requisitesDto);
        BankRequisitesDto newRequisitesDto = new BankRequisitesDto("1111111111111111");

        mockMvc.perform(patch("/seller/account/bank/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequisitesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(newRequisitesDto.getAccount()))
                .andReturn();
    }

    @Test
    @SneakyThrows
    void deleteRequisites_whenOk() {
        createSeller(sellerDto);
        BankRequisitesDto requisitesDto = new BankRequisitesDto("1234567891234567");
        addRequisites(sellerDto.getEmail(), requisitesDto);

        mockMvc.perform(delete("/seller/account/bank/{email}", sellerDto.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    SellerForResponse createSeller(SellerDto sellerDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/seller/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sellerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sellerDto.getName()))
                .andExpect(jsonPath("$.email").value(sellerDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(sellerDto.getPhone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerForResponse.class
        );
    }

    SellerForResponse getSeller(String email) throws Exception {
        MvcResult result = mockMvc.perform(get("/seller/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerForResponse.class
        );
    }

    SellerForResponse updateSeller(String email, SellerForUpdate updateDto) throws Exception {
        MvcResult result = mockMvc.perform(patch("/seller/account/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.email").value(updateDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(updateDto.getPhone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerForResponse.class
        );
    }

    BankRequisitesDto addRequisites(String email, BankRequisitesDto requisitesDto) throws Exception {
        MvcResult result = mockMvc.perform(patch("/seller/account/bank/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requisitesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(requisitesDto.getAccount()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BankRequisitesDto.class
        );
    }

}
