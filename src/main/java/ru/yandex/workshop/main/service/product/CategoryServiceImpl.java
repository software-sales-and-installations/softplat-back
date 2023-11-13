package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.repository.product.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public Category createCategory(Category category) {
        return repository.save(category);
    }

    @Override
    public Category changeCategoryById(Long catId, Category updateRequest) {
        Category category = getCategoryById(catId);
        category.setName(updateRequest.getName());
        return repository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findCategoryAll() {
        return repository.findAll(Pageable.ofSize(10)).stream()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long catId) {
        return repository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
        );
    }

    @Override
    public void deleteCategory(Long catId) {
        getCategoryById(catId);
        repository.deleteById(catId);
    }
}
