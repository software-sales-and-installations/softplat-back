package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.category.CategoriesListResponseDto;
import ru.softplat.main.dto.category.CategoryCreateUpdateDto;
import ru.softplat.main.dto.category.CategoryResponseDto;
import ru.softplat.main.server.model.product.Category;

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
