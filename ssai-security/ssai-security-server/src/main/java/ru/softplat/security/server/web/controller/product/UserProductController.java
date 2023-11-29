package ru.softplat.security.server.web.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.dto.product.ProductCreateUpdateDto;
import ru.softplat.dto.product.ProductResponseDto;
import ru.softplat.dto.product.ProductsListResponseDto;
import ru.softplat.dto.validation.New;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.ProductMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.product.ProductStatus;
import ru.softplat.main.server.service.product.ProductService;
import ru.softplat.main.server.web.validation.MultipartFileFormat;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class UserProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Создание карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductResponseDto createProduct(@ApiIgnore Principal principal, @RequestBody @Validated(New.class) ProductCreateUpdateDto productCreateUpdateDto) {
        log.debug(LogMessage.TRY_CREATE_PRODUCT.label, productCreateUpdateDto);
        Product request = productMapper.productDtoToProduct(productCreateUpdateDto);
        long categoryId = productCreateUpdateDto.getCategory();
        long vendorId = productCreateUpdateDto.getVendor();
        Product response = productService.create(principal.getName(), request, categoryId, vendorId);
        return productMapper.productToProductResponseDto(response);
    }

    @Operation(summary = "Редактирование своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}/update")
    public ProductResponseDto updateProduct(@ApiIgnore Principal principal, @PathVariable Long productId,
                                            @RequestBody @Valid ProductCreateUpdateDto productForUpdate) {
        log.debug(LogMessage.TRY_UPDATE_PRODUCT.label, productId, principal.getName());
        productService.checkSellerAccessRights(principal.getName(), productId);
        Product updateRequest = productMapper.productDtoToProduct(productForUpdate);
        Product response = productService.update(productId, updateRequest);
        return productMapper.productToProductResponseDto(response);
    }

    @Operation(summary = "Отправка своего товара на модерацию админом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}/send")
    public ProductResponseDto updateStatusProductOnSent(@ApiIgnore Principal principal, @PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_SENT.label, productId, principal.getName());
        productService.checkSellerAccessRights(principal.getName(), productId);
        Product response = productService.updateStatus(productId, ProductStatus.SHIPPED);
        return productMapper.productToProductResponseDto(response);
    }

    @Operation(summary = "Отклонение/одобрение карточки товара", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{productId}/moderation")
    public ProductResponseDto updateStatusProductAdmin(@PathVariable Long productId, @RequestParam ProductStatus status) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT.label, productId);
        if (status != ProductStatus.PUBLISHED && status != ProductStatus.REJECTED)
            throw new WrongConditionException("Некорректный статус");
        Product response = productService.updateStatus(productId, status);
        return productMapper.productToProductResponseDto(response);
    }

    @Operation(summary = "Удаление карточки товара", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductAdmin(@PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT.label, productId);
        productService.delete(productId);
    }

    @Operation(summary = "Удаление своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping(path = "/products/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSeller(@ApiIgnore Principal principal, @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT.label, productId);
        productService.checkSellerAccessRights(principal.getName(), productId);
        productService.delete(productId);
    }

    @Operation(summary = "Добавление/обновление изображения своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "/{productId}/image/create")
    public ProductResponseDto createProductImage(@ApiIgnore Principal principal, @PathVariable @Min(1) Long productId,
                                                 @RequestParam(value = "image") @MultipartFileFormat MultipartFile image) {
        log.info(LogMessage.TRY_ADD_IMAGE.label);
        productService.checkSellerAccessRights(principal.getName(), productId);
        Product response = productService.createProductImage(productId, image);
        return productMapper.productToProductResponseDto(response);
    }

    @Operation(summary = "Удаление изображения карточки товара", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(path = "/products/{productId}/image/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageAdmin(@PathVariable @Min(1) Long productId) {
        log.info(LogMessage.TRY_DElETE_IMAGE.label);
        productService.deleteProductImage(productId);
    }

    @Operation(summary = "Удаление изображения своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping(path = "/{productId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageSeller(@ApiIgnore Principal principal, @PathVariable @Min(1) Long productId) {
        log.info(LogMessage.TRY_DElETE_IMAGE.label);
        productService.checkSellerAccessRights(principal.getName(), productId);
        productService.deleteProductImage(productId);
    }

    @Operation(summary = "Получение списка товаров на модерацию", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/shipped")
    public ProductsListResponseDto getAllProductsShipped(
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_ALL_PRODUCTS_SHIPPED.label);
        List<Product> productList = productService.getAllProductsShipped(minId, pageSize);
        List<ProductResponseDto> response = productList.stream()
                .map(productMapper::productToProductResponseDto)
                .collect(Collectors.toList());
        return productMapper.toProductsListResponseDto(response);
    }
}
