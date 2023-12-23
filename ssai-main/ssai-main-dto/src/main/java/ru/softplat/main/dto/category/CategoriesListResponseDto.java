package ru.softplat.main.dto.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesListResponseDto {
    List<CategoryResponseDto> categories;
}
