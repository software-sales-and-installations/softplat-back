package ru.yandex.workshop.main.controller.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.security.dto.UserCreateDto;
import ru.yandex.workshop.security.model.Role;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatsControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private UserCreateDto userDto;

    private ProductResponseDto productResponseDto1;
    private ProductResponseDto productResponseDto2;

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
        productResponseDto1 = getProductResponseDto(1L);
        productResponseDto2 = getProductResponseDto(2L);

    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "joedoe@email.com", authorities = {"seller:write"})
    void getSellerStats() {

    }

}
