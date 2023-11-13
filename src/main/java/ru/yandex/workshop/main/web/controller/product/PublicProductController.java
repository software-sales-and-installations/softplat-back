package ru.yandex.workshop.main.web.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsSearchRequestDto;
import ru.yandex.workshop.main.dto.product.SortBy;
import ru.yandex.workshop.main.mapper.ProductMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.service.product.PublicProductService;

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
    private final PublicProductService productService;
    private final ProductMapper productMapper;

    @GetMapping(path = "/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_GET_PRODUCT_BY_ID.label, productId);
        Product response = productService.getProductById(productId);
        return productMapper.productToProductResponseDto(response);
    }

    @GetMapping(path = "/search")
    public List<ProductResponseDto> searchProducts(
            @RequestBody @Valid ProductsSearchRequestDto productsSearchRequestDto,
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(name = "sort", defaultValue = "NEWEST") SortBy sort) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_FILTER.label);
        List<Product> response = productService.getProductsByFilter(productsSearchRequestDto, minId, pageSize, sort);
        return response.stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
    }
}
