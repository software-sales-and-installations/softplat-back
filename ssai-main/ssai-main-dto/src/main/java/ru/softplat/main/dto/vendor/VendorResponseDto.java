package ru.softplat.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.image.ImageResponseDto;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorResponseDto {
    Long id;
    String name;
    String description;
    ImageResponseDto image;
    Country country;
}
