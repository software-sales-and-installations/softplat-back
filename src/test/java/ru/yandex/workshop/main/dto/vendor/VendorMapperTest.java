package ru.yandex.workshop.main.dto.vendor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.workshop.main.model.vendor.Country;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class VendorMapperTest {
    @Autowired
    private JacksonTester<VendorCreateUpdateDto> jsonVendorDto;

    @Test
    void testItemDto() throws IOException {
        VendorCreateUpdateDto vendorCreateUpdateDto = VendorCreateUpdateDto.builder().name("test").description("test").country(Country.RUSSIA).build();

        JsonContent<VendorCreateUpdateDto> result = jsonVendorDto.write(vendorCreateUpdateDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.country").isEqualTo(Country.RUSSIA.toString());
    }
}