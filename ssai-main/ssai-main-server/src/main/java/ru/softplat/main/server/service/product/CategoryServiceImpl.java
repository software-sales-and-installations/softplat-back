package ru.softplat.main.server.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.product.Category;
import ru.softplat.main.server.repository.product.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public Category createCategory(Category category) {
        checkIfExistsByName(category.getName());
        return repository.save(category);
    }

    @Override
    public Category changeCategoryById(Long catId, Category updateRequest) {
        checkIfExistsByName(updateRequest.getName());
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
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(catId, Category.class)
                ));
    }

    @Override
    public void deleteCategory(Long catId) {
        getCategoryById(catId);
        repository.deleteById(catId);
    }

    private void checkIfExistsByName(String name) {
        if (repository.existsByName(name)) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }
}
