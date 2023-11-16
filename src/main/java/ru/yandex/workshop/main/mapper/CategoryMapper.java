package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.category.CategoryDto;
import ru.yandex.workshop.main.dto.category.CategoryResponseDto;
import ru.yandex.workshop.main.model.product.Category;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    Category categoryDtoToCategory(CategoryDto categoryDto);

    CategoryResponseDto categoryToCategoryResponseDto(Category category);

}
