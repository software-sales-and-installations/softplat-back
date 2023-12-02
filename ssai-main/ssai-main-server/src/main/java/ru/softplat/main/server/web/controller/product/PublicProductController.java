package ru.softplat.main.server.web.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.product.*;
import ru.softplat.main.server.mapper.ProductMapper;
import ru.softplat.main.server.model.product.Product;
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
        List<Product> productList = productService.getProductsByFilter(productsSearchRequestDto, minId, pageSize, sort);
        List<ProductResponseDto> response = productList.stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        ProductsListResponseDto productsListResponseDto = productMapper.toProductsListResponseDto(response);
        if (productsSearchRequestDto == null)
            productsListResponseDto.setTotalProducts(productService.getTotalProductsCount(null));
        else
            productsListResponseDto.setTotalProducts(productService.getTotalProductsCount(ProductStatus.PUBLISHED));
        return productsListResponseDto;
    }

    @GetMapping(path = "/{productId}/similar")
    public ProductsListResponseDto getSimilarProducts(
            @PathVariable Long productId,
            @RequestParam(name = "minId", defaultValue = "0") int minId,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
    ) {
        List<Product> productList = productService.getSimilarProducts(productId, minId, pageSize);
        List<ProductResponseDto> response = productList.stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        ProductsListResponseDto productsListResponseDto = productMapper.toProductsListResponseDto(response);
        productsListResponseDto.setTotalProducts(productService.getTotalProductsCount(ProductStatus.PUBLISHED));
        return productsListResponseDto;
    }
}
