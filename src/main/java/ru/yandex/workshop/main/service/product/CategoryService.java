package ru.yandex.workshop.main.service.product;

import ru.yandex.workshop.main.dto.category.CategoryDto;
import ru.yandex.workshop.main.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryDto categoryDto);

    CategoryResponseDto changeCategoryById(Long catId, CategoryDto categoryDto);

    List<CategoryResponseDto> findCategoryAll();

    CategoryResponseDto findCategoryById(Long catId);

    void deleteCategory(Long catId);
}
