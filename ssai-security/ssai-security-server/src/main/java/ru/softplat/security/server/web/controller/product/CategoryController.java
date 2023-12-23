package ru.softplat.security.server.web.controller.product;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.product.CategoryClient;
import ru.softplat.main.dto.category.CategoriesListResponseDto;
import ru.softplat.main.dto.category.CategoryCreateUpdateDto;
import ru.softplat.main.dto.category.CategoryResponseDto;
import ru.softplat.security.server.message.LogMessage;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/category")
public class CategoryController {
    private final CategoryClient categoryClient;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = CategoriesListResponseDto.class)})
    @Operation(summary = "Получение списка категорий", description = "Доступ для всех")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> findAllCategories() {
        log.debug(LogMessage.TRY_GET_CATEGORY.label);
        return categoryClient.getAllCategories();
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = CategoryResponseDto.class)})
    @Operation(summary = "Получение категории по id", description = "Доступ для всех")
    @GetMapping(path = "/{catId}", produces = "application/json")
    public ResponseEntity<Object> findCategoryById(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_GET_ID_CATEGORY.label, catId);
        return categoryClient.getCategory(catId);
    }

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = CategoryResponseDto.class)})
    @Operation(summary = "Создание категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryCreateUpdateDto categoryCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_CATEGORY.label);
        return categoryClient.createCategory(categoryCreateUpdateDto);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = CategoryResponseDto.class)})
    @Operation(summary = "Изменение категории", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{catId}", produces = "application/json")
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
