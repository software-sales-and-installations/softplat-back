package ru.softplat.security.server.web.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.client.product.ProductClient;
import ru.softplat.main.dto.product.ProductCreateUpdateDto;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.security.server.exception.WrongConditionException;
import ru.softplat.security.server.message.LogMessage;
import ru.softplat.security.server.model.New;
import ru.softplat.security.server.web.validation.MultipartFileFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class UserProductController {

    private final ProductClient productClient;

    @Operation(summary = "Создание карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createProduct(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Validated(New.class) ProductCreateUpdateDto productCreateUpdateDto) {
        log.debug(LogMessage.TRY_CREATE_PRODUCT.label, productCreateUpdateDto);
        return productClient.createProduct(userId, productCreateUpdateDto);
    }

    @Operation(summary = "Редактирование своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}")
    public ResponseEntity<Object> updateProduct(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
                                                @RequestBody @Valid ProductCreateUpdateDto productForUpdate) {
        log.debug(LogMessage.TRY_UPDATE_PRODUCT.label, productId, userId);
        return productClient.updateProduct(userId, productId, productForUpdate);
    }

    @Operation(summary = "Отправка своего товара на модерацию админом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping(path = "/{productId}/send")
    public ResponseEntity<Object> updateStatusProductOnSent(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT_ON_SENT.label, productId, userId);
        return productClient.updateStatusProductOnSent(userId, productId);
    }

    @Operation(summary = "Отклонение/одобрение карточки товара", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{productId}/moderation")
    public ResponseEntity<Object> updateStatusProductAdmin(@PathVariable Long productId, @RequestParam ProductStatus status) {
        log.debug(LogMessage.TRY_UPDATE_STATUS_PRODUCT.label, productId);
        if (status != ProductStatus.PUBLISHED && status != ProductStatus.REJECTED)
            throw new WrongConditionException("Некорректный статус");
        return productClient.updateStatusProductAdmin(productId, status);
    }

    @Operation(summary = "Удаление карточки товара", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductAdmin(@PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT.label, productId);
        productClient.deleteProductAdmin(productId);
    }

    @Operation(summary = "Удаление своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping(path = "/{productId}/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductSeller(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable @Min(1) Long productId) {
        log.debug(LogMessage.TRY_DELETE_PRODUCT.label, productId);
        productClient.deleteProductSeller(userId, productId);
    }

    @Operation(summary = "Добавление/обновление изображения своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "/{productId}/image")
    public ResponseEntity<Object> createProductImage(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable @Min(1) Long productId,
                                                     @RequestParam(value = "image") @MultipartFileFormat MultipartFile image) {
        log.info(LogMessage.TRY_ADD_IMAGE.label);
        return productClient.addProductImage(userId, productId, image);
    }

    @Operation(summary = "Удаление изображения карточки товара", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(path = "/{productId}/image/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageAdmin(@PathVariable @Min(1) Long productId) {
        log.info(LogMessage.TRY_DElETE_IMAGE.label);
        productClient.deleteProductImageAdmin(productId);
    }

    @Operation(summary = "Удаление изображения своей карточки товара", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping(path = "/{productId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductImageSeller(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable @Min(1) Long productId) {
        log.info(LogMessage.TRY_DElETE_IMAGE.label);
        productClient.deleteProductImageSeller(userId, productId);
    }

    @Operation(summary = "Получение списка товаров на модерацию", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/shipped")
    public ResponseEntity<Object> getAllProductsShipped(
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_ALL_PRODUCTS_SHIPPED.label);
        return productClient.getAllProductsShipped(minId, pageSize);
    }

    @Operation(summary = "Просмотр рекомендаций товаров", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/recommendations")
    public ResponseEntity<Object> getProductRecommendations(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "minId", defaultValue = "0") int minId,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
    ) {
        log.info(LogMessage.TRY_BUYER_GET_RECOMMENDATIONS.label, userId);
        return productClient.getProductRecommendations(userId, minId, pageSize);
    }
}
