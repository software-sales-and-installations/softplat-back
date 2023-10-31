package ru.yandex.workshop.main.dto.vendor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class VendorMapperTest {
    @Autowired
    private JacksonTester<VendorDto> jsonVendorDto;

    @Test
    void testItemDto() throws IOException {
        VendorDto vendorDto = VendorDto.builder().name("test").description("test").imageId(1L).country(Country.RUSSIA).build();

        JsonContent<VendorDto> result = jsonVendorDto.write(vendorDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.imageId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.country").isEqualTo(Country.RUSSIA.toString());
    }
}