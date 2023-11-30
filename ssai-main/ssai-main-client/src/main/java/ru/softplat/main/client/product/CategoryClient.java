package ru.softplat.main.client.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.category.CategoryCreateUpdateDto;

@Service
@Slf4j
public class CategoryClient extends BaseClient {
    private static final String API_PREFIX = "/category";

    @Autowired
    public CategoryClient(@Value("${main-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createCategory(CategoryCreateUpdateDto categoryCreateUpdateDto) {
        return post("", categoryCreateUpdateDto);
    }

    public ResponseEntity<Object> getCategory(long catId) {
        return get("/" + catId);
    }

    public ResponseEntity<Object> getAllCategories() {
        return get("");
    }

    public ResponseEntity<Object> updateCategory(long catId, CategoryCreateUpdateDto categoryCreateUpdateDto) {
        return patch("/" + catId, categoryCreateUpdateDto);
    }

    public void deleteCategory(long catId) {
        delete("/" + catId);
    }
}