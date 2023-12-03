package ru.softplat.main.server.web.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.dto.product.ProductsSearchRequestDto;
import ru.softplat.main.dto.product.SortBy;
import ru.softplat.main.server.mapper.ProductMapper;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.product.ProductList;
import ru.softplat.main.server.service.product.SearchProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
public class PublicProductController {
    private final SearchProductService productService;
    private final ProductMapper productMapper;

    @GetMapping(path = "/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        Product response = productService.getProductById(productId);
        return productMapper.productToProductResponseDto(response);
    }

    @GetMapping(path = "/search")
    public ProductsListResponseDto searchProducts(
            @RequestBody(required = false) ProductsSearchRequestDto productsSearchRequestDto,
            @RequestParam int minId, @RequestParam int pageSize, @RequestParam SortBy sort) {
        ProductList productList = productService.getProductsByFilter(productsSearchRequestDto, minId, pageSize, sort);
        List<ProductResponseDto> response = productList.getProducts().stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        return productMapper.toProductsListResponseDto(response, productList.getCount());
    }

    @GetMapping(path = "/{productId}/similar")
    public ProductsListResponseDto getSimilarProducts(
            @PathVariable Long productId,
            @RequestParam(name = "minId", defaultValue = "0") int minId,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
    ) {
        ProductList productList = productService.getSimilarProducts(productId, minId, pageSize);
        List<ProductResponseDto> response = productList.getProducts().stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        return productMapper.toProductsListResponseDto(response, productList.getCount());
    }
}
