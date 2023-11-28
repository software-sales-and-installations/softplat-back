package ru.softplat.main.dto.product;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ProductMapperTest {

    @Autowired
    private JacksonTester<ProductCreateUpdateDto> jsonProductDto;

    @Test
    @SneakyThrows
    void shouldCorrectlyConvertItemDtoToJson() {
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

        ProductCreateUpdateDto productCreateUpdateDto = ProductCreateUpdateDto
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
                .installationPrice(10.00F)
                .build();

        JsonContent<ProductCreateUpdateDto> result = jsonProductDto.write(productCreateUpdateDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("product");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description product");
        assertThat(result).extractingJsonPathStringValue("$.version").isEqualTo("22");
        assertThat(result).extractingJsonPathNumberValue("$.category").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.license").isEqualTo(License.LICENSE.toString());
        assertThat(result).extractingJsonPathNumberValue("$.price").isEqualTo(100.5);
        assertThat(result).extractingJsonPathNumberValue("$.quantity").isEqualTo(5);
        assertThat(result).extractingJsonPathValue("$.installation").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.installationPrice").isEqualTo(10.00);
    }
}