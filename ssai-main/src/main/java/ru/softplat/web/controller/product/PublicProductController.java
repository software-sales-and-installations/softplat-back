package ru.softplat.web.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsListResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsSearchRequestDto;
import ru.yandex.workshop.main.dto.product.SortBy;
import ru.yandex.workshop.main.mapper.ProductMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.service.product.SearchProductService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class PublicProductController {
    private final SearchProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Получение продукта по id", description = "Доступ для всех")
    @GetMapping(path = "/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_GET_PRODUCT_BY_ID.label, productId);
        Product response = productService.getProductById(productId);
        return productMapper.productToProductResponseDto(response);
    }

    @Operation(summary = "Получение списка продуктов/поиск/фильтрация", description = "Доступ для всех")
    @GetMapping(path = "/search")
    public ProductsListResponseDto searchProducts(
            @RequestBody(required = false) @Valid ProductsSearchRequestDto productsSearchRequestDto,
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(name = "sort", defaultValue = "NEWEST") SortBy sort) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_FILTER.label);
        List<Product> productList = productService.getProductsByFilter(productsSearchRequestDto, minId, pageSize, sort);
        List<ProductResponseDto> response = productList.stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        return productMapper.toProductsListResponseDto(response);
    }
}
