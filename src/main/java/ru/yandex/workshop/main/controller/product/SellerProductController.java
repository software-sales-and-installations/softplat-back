package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductForUpdate;
import ru.yandex.workshop.main.service.product.ProductService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = SellerProductController.URL_SELLER)
@Slf4j
@Validated
public class SellerProductController {

    public static final String URL_SELLER = "/seller";

    private final ProductService productService;

    @GetMapping(path = "/{sellerId}/products")
    public List<ProductDto> getProductsSeller(
            @PathVariable @Min(1) Long sellerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/products. GetMapping/Получить все товары продавца/getProductsSeller");
        return productService.getProductsSeller(sellerId, from, size);
    }

    @GetMapping(path = "/{sellerId}/product/{productId}")
    public ProductDto getProductById(
            @PathVariable @Min(1) Long sellerId,
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product/{productId}. GetMapping/Получить товар продавца по id/getProductSellerById");
        return productService.getProductById(sellerId, productId);
    }

    @PostMapping(path = "/product")
    public ProductDto createProduct(
            @RequestBody @Valid ProductDto productDto) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product. PostMapping/Создание товара/createProduct");
        return productService.createProduct(productDto);
    }

    @PatchMapping(path = "/{sellerId}/product")
    public ProductDto updateProduct(
            @PathVariable @Min(1) Long sellerId,
            @RequestBody @Valid ProductForUpdate productForUpdate) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product. PatchMapping/Редактирование товара/updateProduct");
        return productService.updateProduct(sellerId, productForUpdate);
    }

    @PatchMapping(path = "/{sellerId}/product/{productId}/send")
    public ProductDto updateStatusProductOnSent(
            @PathVariable @Min(1) Long sellerId,
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product{productId}/send. " +
                "PatchMapping/Изменение статуса товара на отправленно/updateStatusProductOnSent");
        return productService.updateStatusProductOnSent(sellerId, productId);
    }
}
