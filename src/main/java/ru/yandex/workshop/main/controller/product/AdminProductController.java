package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.service.product.ProductService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = AdminProductController.URL_ADMIN)
@Slf4j
@Validated
public class AdminProductController {

    public static final String URL_ADMIN = "/admin";

    private final ProductService productService;

    @GetMapping(path = "/products")
    public List<ProductDto> getAllProductsSeller(
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.info("URL: " +
                URL_ADMIN
                + "products. GetMapping/Получить все товары продавцов/getAllProductsSeller");
        return productService.getAllProductsSeller(from, size);
    }

    @GetMapping(path = "/{sellerId}/products")
    public List<ProductDto> getProductsSeller(
            @PathVariable @Min(1) Long sellerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.info("URL: " +
                URL_ADMIN
                + "/{sellerId}/products. GetMapping/Получить все товары продавца/getProductsSeller");
        return productService.getProductsSeller(sellerId, from, size);
    }

    @GetMapping(path = "/product/{productId}")
    public ProductDto getProductByIdAdmin(
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_ADMIN
                + "/product/{productId}. GetMapping/Получить товар по id/getProductById");
        return productService.getProductByIdAdmin(productId);
    }

    @PatchMapping(path = "/product/{productId}/published")
    public ProductDto updateStatusProductOnPublished(
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_ADMIN
                + "/product/{productId}/published. " +
                "PatchMapping/Изменение статуса товара на опубликован/updateStatusProductOnPublished");
        return productService.updateStatusProductOnPublished(productId);
    }

    @PatchMapping(path = "/product/{productId}/rejected")
    public ProductDto updateStatusProductOnRejected(
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_ADMIN
                + "/product/{productId}/rejected. " +
                "PatchMapping/Изменение статуса товара на отклонено/updateStatusProductOnRejected");
        return productService.updateStatusProductOnRejected(productId);
    }

    @DeleteMapping(path = "/product/{productId}")
    public void deleteProductAdmin(
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_ADMIN
                + "/product/{productId}. DeleteMapping/Удаление товара/deleteProductAdmin");
        productService.deleteProductAdmin(productId);
    }
}
