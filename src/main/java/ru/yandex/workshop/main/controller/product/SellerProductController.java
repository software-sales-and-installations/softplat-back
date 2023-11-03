package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
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
    public List<ProductResponseDto> getProductsSeller(
            @PathVariable @Min(1) Long sellerId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/products. GetMapping/Получить все товары продавца/getProductsSeller");
        return productService.getProductsSeller(sellerId, from, size);
    }

    @GetMapping(path = "/{sellerId}/product/{productId}")
    public ProductResponseDto getProductById(
            @PathVariable @Min(1) Long sellerId,
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product/{productId}. GetMapping/Получить товар продавца по id/getProductSellerById");
        return productService.getProductById(sellerId, productId);
    }

    @PostMapping(path = "/product")
    public ProductResponseDto createProduct(
            @RequestBody @Valid ProductDto productDto) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product. PostMapping/Создание товара/createProduct");
        return productService.createProduct(productDto);
    }

    @PostMapping(path = "/{sellerId}/product/{productId}/image")
    public ProductResponseDto createProductImage(@PathVariable @Min(1) Long sellerId,
                                         @PathVariable @Min(1) Long productId,
                                         @RequestParam(value = "image") MultipartFile image) {
        log.info(LogMessage.TRY_SELLER_ADD_PRODUCT_IMAGE.label);
        return productService.createProductImage(sellerId, productId, image);
    }

    @PatchMapping(path = "/{sellerId}/product/{productId}")
    public ProductResponseDto updateProduct(
            @PathVariable @Min(1) Long sellerId,
            @PathVariable @Min(1) Long productId,
            @RequestBody @Valid ProductDto productDto) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product. PatchMapping/Редактирование товара/updateProduct");
        return productService.updateProduct(sellerId, productId, productDto);
    }

    @PatchMapping(path = "/{sellerId}/product/{productId}/send")
    public ProductResponseDto updateStatusProductOnSent(
            @PathVariable @Min(1) Long sellerId,
            @PathVariable @Min(1) Long productId) {
        log.info("URL: " +
                URL_SELLER
                + "/{sellerId}/product{productId}/send. " +
                "PatchMapping/Изменение статуса товара на отправленно/updateStatusProductOnSent");
        return productService.updateStatusProductOnSent(sellerId, productId);
    }
}
