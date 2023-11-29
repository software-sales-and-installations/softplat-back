package ru.softplat.main.server.main.controller.vendor;

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
import ru.yandex.workshop.main.controller.AbstractControllerTest;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorSearchRequestDto;
import ru.yandex.workshop.main.dto.vendor.VendorsListResponseDto;
import ru.yandex.workshop.main.model.vendor.Country;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VendorControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private VendorCreateUpdateDto vendorCreateUpdateDto;

    @BeforeEach
    void init() {
        vendorCreateUpdateDto = VendorCreateUpdateDto.builder().name("test").description("test").country(Country.RUSSIA).build();
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void createVendor_whenValid_shouldReturnVendorResponseDto() {
        VendorResponseDto response = createVendor(vendorCreateUpdateDto);

        assertEquals(Long.class, response.getId().getClass());
        assertEquals(vendorCreateUpdateDto.getName(), response.getName());
        assertEquals(vendorCreateUpdateDto.getDescription(), response.getDescription());
        assertEquals(vendorCreateUpdateDto.getCountry(), response.getCountry());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void changeVendorById_shouldReturnVendorResponseDto_whenValid() {
        VendorResponseDto response = createVendor(vendorCreateUpdateDto);

        mvc.perform(patch("/vendor/{vendorId}", response.getId())
                        .content(mapper.writeValueAsString(vendorCreateUpdateDto))
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
    void findVendorsWithFiltersTest_shouldReturnListOfVendor1_whenCountryChina() {
        vendorCreateUpdateDto.setCountry(Country.CHINA);
        VendorResponseDto response = createVendor(vendorCreateUpdateDto);
        VendorSearchRequestDto vendorFilter = new VendorSearchRequestDto("test", List.of(Country.CHINA));
        List<VendorResponseDto> expected = List.of(response);

        MvcResult result = mvc.perform(get("/vendor/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(vendorFilter)))
                .andExpect(status().isOk())
                .andReturn();

        List<VendorResponseDto> actual = mapper.readValue(
                result.getResponse().getContentAsString(),
                VendorsListResponseDto.class).getVendors();

        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void findVendorById_shouldReturnVendor1_whenIdValid() {
        long vendorId = createVendor(vendorCreateUpdateDto).getId();
        VendorResponseDto actual = getVendorResponseDto(vendorId);

        assertEquals(Long.class, actual.getId().getClass());
        assertEquals(vendorCreateUpdateDto.getName(), actual.getName());
        assertEquals(vendorCreateUpdateDto.getDescription(), actual.getDescription());
        assertEquals(vendorCreateUpdateDto.getCountry(), actual.getCountry());
    }
}
