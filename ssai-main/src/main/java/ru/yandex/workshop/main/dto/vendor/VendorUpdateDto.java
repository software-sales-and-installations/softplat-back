package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.main.model.vendor.Country;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorUpdateDto {
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    String name;
    @Length(min = 2, max = 500, message = "Длина имени должна быть от 2 до 500 символов")
    String description;
    Long imageId; //TODO
    Country country;
}
