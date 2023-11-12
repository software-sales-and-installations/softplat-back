package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.service.product.UserProductService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class UserProductController {

    private final UserProductService productService;

    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping
    public ProductResponseDto createProduct(Principal principal, @RequestBody @Valid ProductDto productDto) {
        log.debug(LogMessage.TRY_CREATE_PRODUCT.label, productDto);
        return productService.createProduct(principal.getName(), productDto);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}")
    public ProductResponseDto updateProduct(Principal principal, @PathVariable Long productId,
                                            @RequestBody @Validated(New.class) ProductDto productForUpdate) {
        log.debug(LogMessage.TRY_UPDATE_PRODUCT.label, productId, principal.getName());
        return productService.updateProduct(principal.getName(), productId, productForUpdate);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}/send")
    public ProductResponseDto updateStatusProductOnSent(Principal principal, @PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_SENT.label, productId, principal.getName());
        return productService.updateStatusProductOnSent(principal.getName(), productId);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{productId}/published")
    public ProductResponseDto updateStatusProductOnPublished(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_PUBLISHED.label, productId);
        return productService.updateStatusProduct(productId, ProductStatus.PUBLISHED);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{productId}/rejected")
    public ProductResponseDto updateStatusProductOnRejected(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_REJECTED.label, productId);
        return productService.updateStatusProduct(productId, ProductStatus.REJECTED);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductAdmin(
            @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT.label, productId);
        productService.deleteProduct(productId);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping(path = "/products/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSeller(Principal principal, @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT.label, productId);
        productService.deleteProductSeller(principal.getName(), productId);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping(path = "/{productId}/image")
    public ProductResponseDto createProductImage(Principal principal, @PathVariable @Min(1) Long productId,
                                                 @RequestParam(value = "image") MultipartFile image) {
        log.info(LogMessage.TRY_ADD_IMAGE.label);
        return productService.createProductImage(principal.getName(), productId, image);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/products/{productId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageAdmin(@PathVariable @Min(1) Long productId) {
        log.info(LogMessage.TRY_DElETE_IMAGE.label);
        productService.deleteProductImage(productId);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping(path = "/{productId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageSeller(Principal principal, @PathVariable @Min(1) Long productId) {
        log.info(LogMessage.TRY_DElETE_IMAGE.label);
        productService.deleteProductImageSeller(principal.getName(), productId);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/shipped")
    public List<ProductResponseDto> getAllProductsShipped(
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_ALL_PRODUCTS_SHIPPED.label);
        return productService.getAllProductsShipped(from, size);
    }
}
