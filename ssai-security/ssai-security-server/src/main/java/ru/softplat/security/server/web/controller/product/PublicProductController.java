package ru.softplat.security.server.web.controller.product;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.product.ProductClient;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.dto.product.ProductsSearchRequestDto;
import ru.softplat.main.dto.product.SortBy;
import ru.softplat.security.server.message.LogMessage;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class PublicProductController {
    private final ProductClient productClient;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ProductResponseDto.class)})
    @Operation(summary = "Получение продукта по id", description = "Доступ для всех")
    @GetMapping(path = "/{productId}", produces = "application/json")
    public ResponseEntity<Object> getProductById(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_GET_PRODUCT_BY_ID.label, productId);
        return productClient.getProduct(productId);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ProductsListResponseDto.class)})
    @Operation(summary = "Получение списка продуктов/поиск/фильтрация", description = "Доступ для всех")
    @GetMapping(path = "/search", produces = "application/json")
    public ResponseEntity<Object> searchProducts(
            @RequestBody(required = false) @Valid ProductsSearchRequestDto productsSearchRequestDto,
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(name = "sort", defaultValue = "NEWEST") SortBy sort) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_FILTER.label);
        return productClient.searchProducts(productsSearchRequestDto, minId, pageSize, sort);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ProductsListResponseDto.class)})
    @Operation(summary = "Получения списка похожих товаров", description = "Доступ для всех")
    @GetMapping(path = "/{productId}/similar", produces = "application/json")
    public ResponseEntity<Object> getSimilarProducts(
            @PathVariable Long productId,
            @RequestParam(name = "minId", defaultValue = "0") int minId,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
    ) {
        log.debug(LogMessage.TRY_GET_SIMILAR_PRODUCTS.label);
        return productClient.getSimilarProducts(productId, minId, pageSize);
    }
}
