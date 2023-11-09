package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.service.product.PublicProductService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
@Slf4j
@Validated
public class PublicProductController {
    private final PublicProductService productService;

    // TODO поиск и каталог ПО
    @GetMapping(path = "/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId,
                                             @RequestParam (required = false) License license) {
        log.debug(LogMessage.TRY_GET_PRODUCT_BY_ID.label, productId);
        return productService.getProductById(productId, license);
    }

    @GetMapping(path = "/{sellerId}/products")
    public List<ProductResponseDto> getProductsOfSeller(
            @PathVariable @Min(1) Long sellerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_OF_SELLER.label, sellerId);
        return productService.getProductsOfSeller(sellerId, from, size);
    }
}
