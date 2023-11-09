package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductFilter;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.product.PublicProductService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class PublicProductController {
    private final PublicProductService productService;

    @GetMapping(path = "/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_GET_PRODUCT_BY_ID.label, productId);
        return productService.getProductById(productId);
    }

    @GetMapping(path = "/{sellerId}/products")
    public List<ProductResponseDto> getProductsOfSeller(
            @PathVariable @Min(1) Long sellerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_OF_SELLER.label, sellerId);
        return productService.getProductsOfSeller(sellerId, from, size);
    }

    @GetMapping(path = "/search")
    public List<ProductResponseDto> searchProducts(
            @RequestBody @Valid ProductFilter productFilter,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size,
            @RequestParam(name = "sort", defaultValue = "new") String sort) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_FILTER.label);
        return productService.getProductsByFilter(productFilter, from, size, sort);
    }
}
