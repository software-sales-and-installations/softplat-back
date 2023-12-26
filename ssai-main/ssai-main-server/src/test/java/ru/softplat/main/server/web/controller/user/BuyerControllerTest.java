package ru.softplat.main.server.web.controller.user;

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
import ru.softplat.main.dto.user.BuyerUpdateDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.web.controller.AbstractControllerTest;
import ru.softplat.security.dto.UserCreateMainDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/data-test.sql")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private UserCreateMainDto userDto;

    @BeforeEach
    void init() {
        userDto = UserCreateMainDto.builder()
                .name("buyer1")
                .email("buyer1@email.ru")
                .phone("1111111111")
                .build();
    }

    @Test
    @SneakyThrows
    void getBuyerById_shouldReturnBuyer_whenIdCorrect() {
        final long id = 1;

        BuyerResponseDto actual = getBuyerResponseDto(id);
        assertEquals(userDto.getName(), actual.getName());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getPhone(), actual.getPhone());
    }

    @Test
    @SneakyThrows
    void getBuyerById_shouldThrowException_whenIdIsNotCorrect() {
        final long id = 7;

        mockMvc.perform(get("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    @Test
    @SneakyThrows
    void updateBuyerById_shouldUpdateBuyer_whenRequestDtoIsValid() {
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
    void updateBuyerById_shouldThrowUserNotFoundException_whenIdIsNotCorrect() {
        BuyerUpdateDto updateDto = BuyerUpdateDto.builder()
                .name("Bar")
                .phone("0123456789")
                .email("foobar@email.com")
                .build();

        mockMvc.perform(patch("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 7L)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }

    BuyerResponseDto updateBuyer(BuyerUpdateDto updateDto) throws Exception {
        MvcResult result = mockMvc.perform(patch("/buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
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
