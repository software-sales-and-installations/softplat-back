package ru.softplat.service.product;


import ru.softplat.model.product.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category changeCategoryById(Long catId, Category category);

    List<Category> findCategoryAll();

    Category getCategoryById(Long catId);

    void deleteCategory(Long catId);
}
