package ru.yandex.workshop.main.dto.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.model.vendor.Country;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorResponseDto {
    Long id;
    String name;
    String description;
    ImageResponseDto image;
    Country country;
}
