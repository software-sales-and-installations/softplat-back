package ru.yandex.workshop.main.controller.vendor;

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
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorSearchRequestDto;
import ru.yandex.workshop.main.model.vendor.Country;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VendorControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private VendorDto vendorDto;

    @BeforeEach
    void init() {
        vendorDto = VendorDto.builder().name("test").description("test").country(Country.RUSSIA).build();
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void createVendor_whenValid_shouldReturnVendorResponseDto() {
        VendorResponseDto response = createVendor(vendorDto);

        assertEquals(Long.class, response.getId().getClass());
        assertEquals(vendorDto.getName(), response.getName());
        assertEquals(vendorDto.getDescription(), response.getDescription());
        assertEquals(vendorDto.getCountry(), response.getCountry());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void changeVendorById_whenValid_returnVendorResponseDto() {
        VendorResponseDto response = createVendor(vendorDto);

        mvc.perform(patch("/vendor/{vendorId}", response.getId())
                        .content(mapper.writeValueAsString(vendorDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.country", is(response.getCountry().toString())));
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void findVendorsWithFiltersTest_whenCountryChina_returnListOfVendor1() {
        vendorDto.setCountry(Country.CHINA);
        VendorResponseDto response = createVendor(vendorDto);
        VendorSearchRequestDto vendorFilter = new VendorSearchRequestDto("test", List.of(Country.CHINA));
        List<VendorResponseDto> vendorResponseDtoList = List.of(response);

        mvc.perform(get("/vendor/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(vendorFilter)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(vendorResponseDtoList)));
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void findVendorById_whenIdValid_returnVendor1() {
        long vendorId = createVendor(vendorDto).getId();
        VendorResponseDto response = getVendorResponseDto(vendorId);

        assertEquals(Long.class, response.getId().getClass());
        assertEquals(vendorDto.getName(), response.getName());
        assertEquals(vendorDto.getDescription(), response.getDescription());
        assertEquals(vendorDto.getCountry(), response.getCountry());
    }
}
