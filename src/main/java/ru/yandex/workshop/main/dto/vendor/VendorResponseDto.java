package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorResponseDto {
    Long id;
    String name;
    String description;
    Long imageId;
    String country;
}
