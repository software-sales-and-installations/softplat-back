package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.category.CategoryDto;
import ru.yandex.workshop.main.dto.category.CategoryMapper;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.repository.product.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE
                .categoryToCategoryDto(repository.save(CategoryMapper.INSTANCE.categoryDtoToCategory(categoryDto)));
    }

    @Transactional
    @Override
    public CategoryDto changeCategoryById(Long catId, CategoryDto categoryDto) {
        Category category = availabilityCategory(catId);

        category.setName(categoryDto.getName());

        return CategoryMapper.INSTANCE.categoryToCategoryDto(repository.save(category));
    }

    @Override
    public List<CategoryDto> findCategoryAll() {
        return repository.findAll(Pageable.ofSize(10)).stream()
                .map(CategoryMapper.INSTANCE::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(Long catId) {
        return CategoryMapper.INSTANCE.categoryToCategoryDto(
                availabilityCategory(catId));
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        availabilityCategory(catId);

        repository.deleteById(catId);
    }

    private Category availabilityCategory(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
