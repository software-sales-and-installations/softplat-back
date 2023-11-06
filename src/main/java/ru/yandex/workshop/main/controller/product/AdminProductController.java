package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
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
    public List<ProductResponseDto> getAllProductsSeller(
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_ALL_PRODUCTS_SELLER.label);
        return productService.getAllProductsSeller(from, size);
    }

    @GetMapping(path = "/shipped")
    public List<ProductResponseDto> getAllProductsShipped(
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_ALL_PRODUCTS_SHIPPED.label);
        return productService.getAllProductsShipped(from, size);
    }

    @GetMapping(path = "/{sellerId}/products")
    public List<ProductResponseDto> getProductsSeller(
            @PathVariable @Min(1) Long sellerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_ADMIN.label, sellerId);
        return productService.getProductsSeller(sellerId, from, size);
    }

    @GetMapping(path = "/product/{productId}")
    public ProductResponseDto getProductByIdAdmin(
            @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_BY_ID_ADMIN.label, productId);
        return productService.getProductByIdAdmin(productId);
    }

    @PatchMapping(path = "/product/{productId}/published")
    public ProductResponseDto updateStatusProductOnPublished(
            @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_PUBLISHED.label, productId);
        return productService.updateStatusProductOnPublished(productId);
    }

    @PatchMapping(path = "/product/{productId}/rejected")
    public ProductResponseDto updateStatusProductOnRejected(
            @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_REJECTED.label, productId);
        return productService.updateStatusProductOnRejected(productId);
    }

    @DeleteMapping(path = "/product/{productId}")
    public void deleteProductAdmin(
            @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT_ADMIN.label, productId);
        productService.deleteProductAdmin(productId);
    }
}
