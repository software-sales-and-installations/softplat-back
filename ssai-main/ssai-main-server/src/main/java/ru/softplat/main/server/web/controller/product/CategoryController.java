package ru.softplat.main.server.web.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.category.CategoriesListResponseDto;
import ru.softplat.main.dto.category.CategoryCreateUpdateDto;
import ru.softplat.main.dto.category.CategoryResponseDto;
import ru.softplat.main.server.mapper.CategoryMapper;
import ru.softplat.main.server.model.product.Category;
import ru.softplat.main.server.service.product.CategoryServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryServiceImpl service;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public CategoriesListResponseDto findAllCategories() {
        List<CategoryResponseDto> response = service.findCategoryAll().stream()
                .map(categoryMapper::categoryToCategoryResponseDto)
                .collect(Collectors.toList());
        return categoryMapper.toCategoriesListResponseDto(response);
    }

    @GetMapping(path = "/{catId}")
    public CategoryResponseDto findCategoryById(@PathVariable Long catId) {
        Category response = service.getCategoryById(catId);
        return categoryMapper.categoryToCategoryResponseDto(response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody CategoryCreateUpdateDto categoryCreateUpdateDto) {
        Category request = categoryMapper.categoryDtoToCategory(categoryCreateUpdateDto);
        Category response = service.createCategory(request);
        return categoryMapper.categoryToCategoryResponseDto(response);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryResponseDto changeCategoryById(@PathVariable Long catId,
                                                  @RequestBody CategoryCreateUpdateDto categoryCreateUpdateDto) {
        Category updateRequest = categoryMapper.categoryDtoToCategory(categoryCreateUpdateDto);
        Category response = service.changeCategoryById(catId, updateRequest);
        return categoryMapper.categoryToCategoryResponseDto(response);
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        service.deleteCategory(catId);
    }
}
