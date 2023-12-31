package ru.softplat.main.server.web.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.main.dto.product.ProductCreateUpdateDto;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.server.mapper.ImageMapper;
import ru.softplat.main.server.mapper.ProductMapper;
import ru.softplat.main.server.model.image.Image;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.product.ProductList;
import ru.softplat.main.server.service.complaint.ComplaintService;
import ru.softplat.main.server.service.product.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
public class UserProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final ComplaintService complaintService;

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
        complaintService.updateProductComplaintsBySeller(productId);
        return productMapper.productToProductResponseDto(response);
    }

    @PatchMapping(path = "/{productId}/send")
    public ProductResponseDto updateStatusProductOnSent(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PathVariable Long productId) {
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
                                                 @RequestBody ImageCreateDto imageCreateDto) {
        productService.checkSellerAccessRights(userId, productId);
        Image image = imageMapper.toImage(imageCreateDto);
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

    @GetMapping(path = "/admin")
    public ProductsListResponseDto getAllProductsAdmin(
            @RequestParam int minId, @RequestParam int pageSize, @RequestParam ProductStatus status) {
        ProductList productList = productService.getAllProductsAdminByStatus(minId, pageSize, status);
        return productMapper.toProductsListResponseDto(productList);
    }

    @GetMapping(path = "/seller")
    public ProductsListResponseDto getAllProductsSeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam int minId, @RequestParam int pageSize,
                                                        @RequestParam ProductStatus status) {
        ProductList productList = productService.getAllProductsSellerByStatus(userId, minId, pageSize, status);
        return productMapper.toProductsListResponseDto(productList);
    }

    @PostMapping("/{productId}/demo")
    public void loadDemo(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long productId) {
        productService.downloadDemo(userId, productId);
    }
}
