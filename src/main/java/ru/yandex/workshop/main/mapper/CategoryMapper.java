package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.category.CategoriesListResponseDto;
import ru.yandex.workshop.main.dto.category.CategoryCreateUpdateDto;
import ru.yandex.workshop.main.dto.category.CategoryResponseDto;
import ru.yandex.workshop.main.model.product.Category;

import java.util.List;

@Mapper
@Component
public interface CategoryMapper {
    Category categoryDtoToCategory(CategoryCreateUpdateDto categoryCreateUpdateDto);

    CategoryResponseDto categoryToCategoryResponseDto(Category category);

    default CategoriesListResponseDto toCategoriesListResponseDto(List<CategoryResponseDto> categories) {
        return CategoriesListResponseDto.builder().categories(categories).build();
    }
}
