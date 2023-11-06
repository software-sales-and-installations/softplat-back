package ru.yandex.workshop.main.service.product;

import ru.yandex.workshop.main.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto changeCategoryById(Long catId, CategoryDto categoryDto);

    List<CategoryDto> findCategoryAll();

    CategoryDto findCategoryById(Long catId);

    void deleteCategory(Long catId);
}
