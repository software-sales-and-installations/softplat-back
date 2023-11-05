package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.category.CategoryDto;
import ru.yandex.workshop.main.dto.category.CategoryMapper;
import ru.yandex.workshop.main.dto.category.CategoryResponseDto;
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
    public CategoryResponseDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE
                .categoryToCategoryResponseDto(repository
                        .save(CategoryMapper.INSTANCE
                                .categoryDtoToCategory(categoryDto)));
    }

    @Transactional
    @Override
    public CategoryResponseDto changeCategoryById(Long catId, CategoryDto categoryDto) {
        Category oldCategory = availabilityCategory(catId);

        if (categoryDto.getName() != null) oldCategory.setName(categoryDto.getName());

        return CategoryMapper.INSTANCE.categoryToCategoryResponseDto(repository.save(oldCategory));
    }

    public List<CategoryResponseDto> findCategoryAll() {
        return repository.findAll(Pageable.ofSize(10)).stream()
                .map(CategoryMapper.INSTANCE::categoryToCategoryResponseDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto findCategoryById(Long catId) {
        return CategoryMapper.INSTANCE.categoryToCategoryResponseDto(
                repository.findById(catId)
                        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    @Override
    public void deleteCategory(Long catId) {
        repository.deleteById(catId);
    }

    private Category availabilityCategory(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
