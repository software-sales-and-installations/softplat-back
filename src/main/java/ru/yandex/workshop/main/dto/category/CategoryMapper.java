package ru.yandex.workshop.main.dto.category;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.model.product.Category;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponseDto categoryToCategoryResponseDto(Category category);

    Category categoryDtoToCategory(CategoryDto categoryDto);

}
