package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.category.CategoryDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.product.CategoryServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    private final CategoryServiceImpl service;

    @GetMapping
    public List<CategoryDto> findAllCategories() {
        log.debug(LogMessage.TRY_GET_CATEGORY.label);
        return service.findCategoryAll();
    }

    @GetMapping(path = "/{catId}")
    public CategoryDto findCategoryById(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_GET_ID_CATEGORY.label, catId);
        return service.findCategoryById(catId);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_CATEGORY.label);
        return service.createCategory(categoryDto);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{catId}")
    public CategoryDto changeCategoryById(@PathVariable(name = "catId") Long catId,
                                          @RequestBody @Valid CategoryDto categoryDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_CATEGORY.label, catId);
        return service.changeCategoryById(catId, categoryDto);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{catId}")
    public void deleteCategory(@PathVariable(name = "catId") Long catId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_CATEGORY.label, catId);
        service.deleteCategory(catId);
    }
}
