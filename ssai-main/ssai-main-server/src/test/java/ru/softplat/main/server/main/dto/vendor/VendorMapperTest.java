package ru.softplat.main.server.main.dto.vendor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.workshop.main.model.vendor.Country;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class VendorMapperTest {
    @Autowired
    private JacksonTester<VendorCreateUpdateDto> jsonVendorDto;

    @Test
    @SneakyThrows
    void shouldCorrectlyConvertItemDtoToJson() {
        VendorCreateUpdateDto vendorCreateUpdateDto = VendorCreateUpdateDto.builder().name("test").description("test").country(Country.RUSSIA).build();

        JsonContent<VendorCreateUpdateDto> result = jsonVendorDto.write(vendorCreateUpdateDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.country").isEqualTo(Country.RUSSIA.toString());
    }
}