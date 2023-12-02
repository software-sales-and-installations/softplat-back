package ru.softplat.main.dto.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.image.ImageResponseDto;

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
