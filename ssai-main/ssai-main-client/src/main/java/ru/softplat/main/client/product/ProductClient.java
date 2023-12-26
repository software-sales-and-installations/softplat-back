package ru.softplat.main.client.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.main.dto.product.ProductCreateUpdateDto;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.dto.product.ProductsSearchRequestDto;
import ru.softplat.main.dto.product.SortBy;

import java.util.Map;

@Service
public class ProductClient extends BaseClient {
    private static final String API_PREFIX = "/product";

    @Autowired
    public ProductClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createProduct(long userId, ProductCreateUpdateDto productCreateUpdateDto) {
        return post("", userId, productCreateUpdateDto);
    }

    public ResponseEntity<Object> getProduct(long productId) {
        return get("/" + productId);
    }

    public ResponseEntity<Object> searchProducts(@Nullable ProductsSearchRequestDto productsSearchRequestDto,
                                                      int minId, int pageSize, SortBy sort) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize,
                "sort", sort
        );
        return post("/search?minId={minId}&pageSize={pageSize}&sort={sort}", null, parameters, productsSearchRequestDto);
    }

    public ResponseEntity<Object> updateProduct(long userId, long productId, ProductCreateUpdateDto productCreateUpdateDto) {
        return patch("/" + productId + "/update", userId, productCreateUpdateDto);
    }

    public ResponseEntity<Object> updateStatusProductOnSent(long userId, long productId) {
        return patch("/" + productId + "/send", userId);
    }

    public ResponseEntity<Object> updateStatusProductAdmin(long productId, ProductStatus status) {
        Map<String, Object> parameters = Map.of(
                "status", status
        );
        return patch("/" + productId + "/moderation?status={status}", null, parameters, null);
    }

    public void deleteProductAdmin(long productId) {
        delete("/" + productId + "/delete");
    }

    public void deleteProductSeller(long userId, long productId) {
        delete("/" + productId, userId);
    }

    public ResponseEntity<Object> addProductImage(long userId, long productId, ImageCreateDto image) {
        return post("/" + productId + "/image/create", userId, image);
    }

    public void deleteProductImageAdmin(long productId) {
        delete("/" + productId + "/image/delete");
    }

    public void deleteProductImageSeller(long userId, long productId) {
        delete("/" + productId + "/image", userId);
    }

    public ResponseEntity<Object> getAllProductsAdmin(int minId, int pageSize, ProductStatus status) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize,
                "status", status
        );
        return get("/admin?minId={minId}&pageSize={pageSize}&status={status}", parameters);
    }

    public ResponseEntity<Object> getAllProductsSeller(long userId, int minId, int pageSize, ProductStatus status) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize,
                "status", status
        );
        return get("/seller?minId={minId}&pageSize={pageSize}&status={status}", userId, parameters);
    }

    public ResponseEntity<Object> getSimilarProducts(long productId, int minId, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize
        );
        return get("/" + productId + "/similar?minId={minId}&pageSize={pageSize}", parameters);
    }

    public void downloadDemo(long userId, long productId) {
        post("/" + productId + "/demo", userId, null);
    }
}