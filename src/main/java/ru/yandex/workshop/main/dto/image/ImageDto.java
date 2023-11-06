package ru.yandex.workshop.main.dto.image;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageDto {
    Long id;
    String name;
    Long size;
    String contentType;
    byte[] bytes;
}
