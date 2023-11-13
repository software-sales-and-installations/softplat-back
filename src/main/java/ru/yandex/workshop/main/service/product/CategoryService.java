package ru.yandex.workshop.main.service.product;

import ru.yandex.workshop.main.model.product.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category changeCategoryById(Long catId, Category category);

    List<Category> findCategoryAll();

    Category getCategoryById(Long catId);

    void deleteCategory(Long catId);
}
