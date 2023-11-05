package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductForUpdate;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.service.product.UserProductService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class UserProductController {

    private final UserProductService productService;

    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping(path = "/product")
    public ProductResponseDto createProduct(Principal principal, @RequestBody @Valid ProductDto productDto) {
        log.debug(LogMessage.TRY_CREATE_PRODUCT.label, productDto);
        return productService.createProduct(principal.getName(), productDto);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}")
    public ProductResponseDto updateProduct(Principal principal, @PathVariable Long productId,
            @RequestBody @Valid ProductForUpdate productForUpdate) {
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
    @PatchMapping(path = "/product/{productId}/published")
    public ProductResponseDto updateStatusProductOnPublished(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_PUBLISHED.label, productId);
        return productService.updateStatusProduct(productId, ProductStatus.PUBLISHED);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/product/{productId}/rejected")
    public ProductResponseDto updateStatusProductOnRejected(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_REJECTED.label, productId);
        return productService.updateStatusProduct(productId, ProductStatus.REJECTED);
    }

    @PreAuthorize("hasAuthority('admin:write') || hasAuthority('seller:write')")
    @DeleteMapping(path = "/product/{productId}")
    public void deleteProductAdmin(
            @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT_ADMIN.label, productId);
        productService.deleteProduct(productId);
    }
}
