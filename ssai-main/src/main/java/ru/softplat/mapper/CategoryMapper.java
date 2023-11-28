package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.dto.category.CategoriesListResponseDto;
import ru.softplat.dto.category.CategoryCreateUpdateDto;
import ru.softplat.dto.category.CategoryResponseDto;
import ru.softplat.model.product.Category;


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
