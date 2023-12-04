package ru.softplat.security.server.web.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.product.CategoryClient;
import ru.softplat.main.dto.category.CategoryCreateUpdateDto;
import ru.softplat.security.server.message.LogMessage;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/category")
public class CategoryController {
    private final CategoryClient categoryClient;

    @Operation(summary = "Получение списка категорий", description = "Доступ для всех")
    @GetMapping
    public ResponseEntity<Object> findAllCategories() {
        log.debug(LogMessage.TRY_GET_CATEGORY.label);
        return categoryClient.getAllCategories();
    }

    @Operation(summary = "Получение категории по id", description = "Доступ для всех")
    @GetMapping(path = "/{catId}")
    public ResponseEntity<Object> findCategoryById(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_GET_ID_CATEGORY.label, catId);
        return categoryClient.getCategory(catId);
    }

    @Operation(summary = "Создание категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryCreateUpdateDto categoryCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_CATEGORY.label);
        return categoryClient.createCategory(categoryCreateUpdateDto);
    }

    @Operation(summary = "Изменение категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{catId}")
    public ResponseEntity<Object> changeCategoryById(@PathVariable(name = "catId") Long catId,
                                                     @RequestBody @Valid CategoryCreateUpdateDto categoryCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_CATEGORY.label, catId);
        return categoryClient.updateCategory(catId, categoryCreateUpdateDto);
    }

    @Operation(summary = "Удаление категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_CATEGORY.label, catId);
        categoryClient.deleteCategory(catId);
    }
}
