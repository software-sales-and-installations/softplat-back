package ru.softplat.dto.image;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageResponseDto {
    Long id;
    String name;
    Long size;
    String url;
    String contentType;
}
