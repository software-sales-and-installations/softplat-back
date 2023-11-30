package ru.softplat.main.server.web.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.dto.product.ProductCreateUpdateDto;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.server.mapper.ProductMapper;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.service.product.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
public class UserProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductResponseDto createProduct(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody ProductCreateUpdateDto productCreateUpdateDto) {
        Product request = productMapper.productDtoToProduct(productCreateUpdateDto);
        long categoryId = productCreateUpdateDto.getCategory();
        long vendorId = productCreateUpdateDto.getVendor();
        Product response = productService.create(userId, request, categoryId, vendorId);
        return productMapper.productToProductResponseDto(response);
    }

    @PatchMapping(path = "/{productId}/update")
    public ProductResponseDto updateProduct(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
                                            @RequestBody ProductCreateUpdateDto productForUpdate) {
        productService.checkSellerAccessRights(userId, productId);
        Product updateRequest = productMapper.productDtoToProduct(productForUpdate);
        Product response = productService.update(productId, updateRequest);
        return productMapper.productToProductResponseDto(response);
    }

    @PatchMapping(path = "/{productId}/send")
    public ProductResponseDto updateStatusProductOnSent(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId) {
        productService.checkSellerAccessRights(userId, productId);
        Product response = productService.updateStatus(productId, ProductStatus.SHIPPED);
        return productMapper.productToProductResponseDto(response);
    }

    @PatchMapping(path = "/{productId}/moderation")
    public ProductResponseDto updateStatusProductAdmin(@PathVariable Long productId, @RequestParam ProductStatus status) {
        Product response = productService.updateStatus(productId, status);
        return productMapper.productToProductResponseDto(response);
    }

    @DeleteMapping(path = "/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductAdmin(@PathVariable Long productId) {
        productService.delete(productId);
    }

    @DeleteMapping(path = "/products/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSeller(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId) {
        productService.checkSellerAccessRights(userId, productId);
        productService.delete(productId);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "/{productId}/image/create")
    public ProductResponseDto createProductImage(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
                                                 @RequestParam(value = "image") MultipartFile image) {
        productService.checkSellerAccessRights(userId, productId);
        Product response = productService.createProductImage(productId, image);
        return productMapper.productToProductResponseDto(response);
    }

    @PostMapping(path = "/products/{productId}/image/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageAdmin(@PathVariable Long productId) {
        productService.deleteProductImage(productId);
    }

    @DeleteMapping(path = "/{productId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageSeller(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId) {
        productService.checkSellerAccessRights(userId, productId);
        productService.deleteProductImage(productId);
    }

    @GetMapping(path = "/shipped")
    public ProductsListResponseDto getAllProductsShipped(
            @RequestParam int minId, @RequestParam int pageSize) {
        List<Product> productList = productService.getAllProductsShipped(minId, pageSize);
        List<ProductResponseDto> response = productList.stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        return productMapper.toProductsListResponseDto(response);
    }
}
