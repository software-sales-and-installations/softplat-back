package ru.yandex.workshop.main.dto.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ProductMapperTest {

    @Autowired
    private JacksonTester<ProductDto> jsonProductDto;

    LocalDateTime time = LocalDateTime.of(LocalDate.of(2023, 11, 1),
            LocalTime.of(22, 21, 41, 555));

    @Test
    void testItemDto() throws IOException {
        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String foramttedString = time.format(aFormatter);

        ProductDto productDto = ProductDto
                .builder()
                .name("product")
                .description("description product")
                .version("22")
                .productionTime(time)
                .image(Image
                        .builder()
                        .name("Image")
                        .size(10.5F)
                        .contentType("contentType")
                        .bytes(new byte[]{0x01, 0x02, 0x03})
                        .build())
                .category(
                        Category
                                .builder()
                                .id(1L)
                                .name("category")
                                .build())
                .license(License.LICENSE)
                .vendor(
                        Vendor
                                .builder()
                                .name("vendor")
                                .description("description test")
                                .imageId(1L)
                                .country(Country.RUSSIA)
                                .build())
                .seller(
                        Seller
                                .builder()
                                .id(1L)
                                .email("aamamama_ma@mail.ru")
                                .name("seller")
                                .phone("+79111111111")
                                .description("description seller")
                                .registrationTime(time.minusYears(5L))
                                .requisites(BankRequisites
                                        .builder()
                                        .id(1L)
                                        .account("1111 2222 3333 4444")
                                        .build())
                                .image(Image
                                        .builder()
                                        .name("Image")
                                        .size(10.5F)
                                        .contentType("contentType")
                                        .bytes(new byte[]{0x01, 0x02, 0x03})
                                        .build())
                                .build())
                .price(100.5F)
                .quantity(5)
                .installation(true)
                .productStatus(ProductStatus.DRAFT)
                .productAvailability(true)
                .build();
        JsonContent<ProductDto> result = jsonProductDto.write(productDto);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("product");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description product");
        assertThat(result).extractingJsonPathStringValue("$.version").isEqualTo("22");
        assertThat(result).extractingJsonPathStringValue("$.productionTime").isEqualTo(foramttedString);
        assertThat(result).extractingJsonPathStringValue("$.image.name").isEqualTo("Image");
        assertThat(result).extractingJsonPathNumberValue("$.image.size").isEqualTo(10.5);
        assertThat(result).extractingJsonPathStringValue("$.image.contentType").isEqualTo("contentType");
        //TODO - добавить проверку bytes
        assertThat(result).extractingJsonPathStringValue("$.category.name").isEqualTo("category");
        assertThat(result).extractingJsonPathStringValue("$.license").isEqualTo(License.LICENSE.toString());
        assertThat(result).extractingJsonPathStringValue("$.vendor.name").isEqualTo("vendor");
        assertThat(result).extractingJsonPathStringValue("$.vendor.description").isEqualTo("description test");
        assertThat(result).extractingJsonPathNumberValue("$.vendor.imageId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.vendor.country").isEqualTo(Country.RUSSIA.toString());
        assertThat(result).extractingJsonPathStringValue("$.seller.email").isEqualTo("aamamama_ma@mail.ru");
        assertThat(result).extractingJsonPathStringValue("$.seller.name").isEqualTo("seller");
        assertThat(result).extractingJsonPathStringValue("$.seller.phone").isEqualTo("+79111111111");
        assertThat(result).extractingJsonPathStringValue("$.seller.description").isEqualTo("description seller");
        assertThat(result).extractingJsonPathStringValue("$.seller.registrationTime").isEqualTo(time.minusYears(5L).toString());
        assertThat(result).extractingJsonPathStringValue("$.seller.requisites.account").isEqualTo("1111 2222 3333 4444");
        assertThat(result).extractingJsonPathStringValue("$.seller.image.name").isEqualTo("Image");
        assertThat(result).extractingJsonPathNumberValue("$.seller.image.size").isEqualTo(10.5);
        assertThat(result).extractingJsonPathStringValue("$.seller.image.contentType").isEqualTo("contentType");
        assertThat(result).extractingJsonPathNumberValue("$.price").isEqualTo(100.5);
        assertThat(result).extractingJsonPathNumberValue("$.quantity").isEqualTo(5);
        assertThat(result).extractingJsonPathValue("$.installation").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.productStatus").isEqualTo(ProductStatus.DRAFT.toString());
        assertThat(result).extractingJsonPathValue("$.productAvailability").isEqualTo(true);
    }
}