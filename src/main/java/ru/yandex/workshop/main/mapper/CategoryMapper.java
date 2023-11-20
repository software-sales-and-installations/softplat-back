package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.category.CategoryCreateUpdateDto;
import ru.yandex.workshop.main.dto.category.CategoryResponseDto;
import ru.yandex.workshop.main.model.product.Category;

@Mapper
@Component
public interface CategoryMapper {
    Category categoryDtoToCategory(CategoryCreateUpdateDto categoryCreateUpdateDto);

    CategoryResponseDto categoryToCategoryResponseDto(Category category);

}
