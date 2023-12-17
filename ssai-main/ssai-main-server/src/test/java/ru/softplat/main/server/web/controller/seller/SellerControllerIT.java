package ru.softplat.main.server.web.controller.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.dto.seller.LegalForm;
import ru.softplat.main.dto.user.SellerUpdateDto;
import ru.softplat.main.dto.user.response.SellerResponseDto;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.web.controller.AbstractControllerTest;
import ru.softplat.security.dto.UserCreateMainDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SellerControllerIT extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private UserCreateMainDto userDto;
    private BankRequisitesCreateUpdateDto requisites;

    @BeforeEach
    void init() {
        userDto = UserCreateMainDto.builder()
                .name("seller1")
                .email("seller1@email.ru")
                .phone("1111111111")
                .build();
        requisites = BankRequisitesCreateUpdateDto.builder()
                .account("123456789")
                .inn("1234567890")
                .legalForm(LegalForm.OAO)
                .bik("123456789")
                .kpp("1234567890")
                .ogrn("1234567891234")
                .address("г.Москва, ул.Тверская, д.10")
                .build();
    }

    @Test
    @SneakyThrows
    void getSeller_shouldReturnSeller_whenIdCorrect() {
        final long id = 1;

        SellerResponseDto actual = getSellerResponseDto(id);

        assertEquals(userDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    void updateSeller_shouldReturnUpdatedSeller_whenIdIsCorrect() {
        SellerUpdateDto updateDto = SellerUpdateDto.builder()
                .name("Bar")
                .phone("0123456789")
                .build();

        SellerResponseDto actual = updateSeller(updateDto);

        assertEquals(updateDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(updateDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    void updateSellerById_shouldThrowUserNotFoundException_whenIdIsNotCorrect() {
        SellerUpdateDto updateDto = SellerUpdateDto.builder()
                .name("Bar")
                .phone("0123456789")
                .build();

        mockMvc.perform(patch("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10L)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    void addRequisites_shouldReturnRequisites_whenRequestDtoIsValid() {
        BankRequisitesCreateUpdateDto actual = addRequisites(requisites);

        assertEquals(requisites.getAccount(), actual.getAccount());
        assertEquals(requisites.getInn(), actual.getInn());
        assertEquals(requisites.getLegalForm(), actual.getLegalForm());
        assertEquals(requisites.getBik(), actual.getBik());
        assertEquals(requisites.getKpp(), actual.getKpp());
        assertNull(actual.getOgrnip());
        assertEquals(requisites.getOgrn(), actual.getOgrn());
    }

    @Test
    @SneakyThrows
    void updateRequisites_shouldReturnUpdatedRequisites_whenRequestDtoIsValid() {
        addRequisites(requisites);
        BankRequisitesCreateUpdateDto newRequisitesDto = BankRequisitesCreateUpdateDto.builder()
                .legalForm(LegalForm.IP)
                .inn("123456789123")
                .ogrnip("123456789123")
                .build();

        MvcResult result = mockMvc.perform(patch("/seller/bank")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequisitesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inn").value(newRequisitesDto.getInn()))
                .andExpect(jsonPath("$.legalForm").value(String.valueOf(newRequisitesDto.getLegalForm())))
                .andExpect(jsonPath("$.ogrnip").value(newRequisitesDto.getOgrnip()))
                .andReturn();
        BankRequisitesResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BankRequisitesResponseDto.class);

        assertEquals(requisites.getAccount(), actual.getAccount());
        assertEquals(newRequisitesDto.getInn(), actual.getInn());
        assertEquals(newRequisitesDto.getLegalForm(), actual.getLegalForm());
        assertEquals(requisites.getBik(), actual.getBik());
        assertEquals(requisites.getKpp(), actual.getKpp());
        assertNull(actual.getOgrn());
        assertEquals(newRequisitesDto.getOgrnip(), actual.getOgrnip());
    }

    @Test
    @SneakyThrows
    void deleteRequisites_shouldExecuteAndReturnStatusNoContent() {
        addRequisites(requisites);

        mockMvc.perform(delete("/seller/bank")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    SellerResponseDto updateSeller(SellerUpdateDto updateDto) {
        MvcResult result = mockMvc.perform(patch("/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", (long) 1)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((long) 1))
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.phone").value(updateDto.getPhone()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerResponseDto.class
        );
    }

    @SneakyThrows
    BankRequisitesCreateUpdateDto addRequisites(BankRequisitesCreateUpdateDto requisitesDto) {
        MvcResult result = mockMvc.perform(post("/seller/bank")
                        .header("X-Sharer-User-Id", (long) 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requisitesDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account").value(requisitesDto.getAccount()))
                .andExpect(jsonPath("$.inn").value(requisitesDto.getInn()))
                .andExpect(jsonPath("$.legalForm").value(String.valueOf(requisitesDto.getLegalForm())))
                .andExpect(jsonPath("$.bik").value(requisitesDto.getBik()))
                .andExpect(jsonPath("$.kpp").value(requisitesDto.getKpp()))
                .andExpect(jsonPath("$.ogrn").value(requisitesDto.getOgrn()))
                .andExpect(jsonPath("$.address").value(requisitesDto.getAddress()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BankRequisitesCreateUpdateDto.class
        );
    }
}
