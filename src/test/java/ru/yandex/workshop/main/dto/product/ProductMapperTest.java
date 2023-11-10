package ru.yandex.workshop.main.dto.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ProductMapperTest {

    @Autowired
    private JacksonTester<ProductDto> jsonProductDto;

    LocalDateTime time = LocalDateTime.of(LocalDate.of(2023, 11, 1),
            LocalTime.of(22, 21, 41, 555));

    @Test
    void testItemDto() throws IOException {
        Category category = Category
                .builder()
                .id(1L)
                .name("category")
                .build();

        Vendor vendor = Vendor
                .builder()
                .id(1L)
                .name("vendor")
                .description("description test")
                .country(Country.RUSSIA)
                .build();

        BankRequisites requisites = BankRequisites
                .builder()
                .id(1L)
                .account("1111 2222 3333 4444")
                .build();

        Image image = Image
                .builder()
                .name("Image")
                .size(10L)
                .contentType("contentType")
                .bytes(new byte[]{0x01, 0x02, 0x03})
                .build();

        ProductDto productDto = ProductDto
                .builder()
                .name("product")
                .description("description product")
                .version("22")
                .category(category.getId())
                .license(License.LICENSE)
                .vendor(vendor.getId())
                .price(100.5F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        JsonContent<ProductDto> result = jsonProductDto.write(productDto);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("product");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description product");
        assertThat(result).extractingJsonPathStringValue("$.version").isEqualTo("22");
        assertThat(result).extractingJsonPathNumberValue("$.category").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.license").isEqualTo(License.LICENSE.toString());
        assertThat(result).extractingJsonPathNumberValue("$.price").isEqualTo(100.5);
        assertThat(result).extractingJsonPathNumberValue("$.quantity").isEqualTo(5);
        assertThat(result).extractingJsonPathValue("$.installation").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.productAvailability").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.installationPrice").isEqualTo(10.00);
    }
}