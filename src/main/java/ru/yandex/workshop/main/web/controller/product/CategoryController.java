package ru.yandex.workshop.main.web.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.category.CategoryCreateUpdateDto;
import ru.yandex.workshop.main.dto.category.CategoryResponseDto;
import ru.yandex.workshop.main.mapper.CategoryMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.service.product.CategoryServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    private final CategoryServiceImpl service;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Получение списка категорий", description = "Доступ для всех")
    @GetMapping
    public List<CategoryResponseDto> findAllCategories() {
        log.debug(LogMessage.TRY_GET_CATEGORY.label);
        List<Category> response = service.findCategoryAll();
        return response.stream()
                .map(categoryMapper::categoryToCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получение категории по id", description = "Доступ для всех")
    @GetMapping(path = "/{catId}")
    public CategoryResponseDto findCategoryById(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_GET_ID_CATEGORY.label, catId);
        Category response = service.getCategoryById(catId);
        return categoryMapper.categoryToCategoryResponseDto(response);
    }

    @Operation(summary = "Создание категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryCreateUpdateDto categoryCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_CATEGORY.label);
        Category request = categoryMapper.categoryDtoToCategory(categoryCreateUpdateDto);
        Category response = service.createCategory(request);
        return categoryMapper.categoryToCategoryResponseDto(response);
    }

    @Operation(summary = "Изменение категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{catId}")
    public CategoryResponseDto changeCategoryById(@PathVariable(name = "catId") Long catId,
                                          @RequestBody @Valid CategoryCreateUpdateDto categoryCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_CATEGORY.label, catId);
        Category updateRequest = categoryMapper.categoryDtoToCategory(categoryCreateUpdateDto);
        Category response = service.changeCategoryById(catId, updateRequest);
        return categoryMapper.categoryToCategoryResponseDto(response);
    }

    @Operation(summary = "Удаление категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_CATEGORY.label, catId);
        service.deleteCategory(catId);
    }
}
