package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.dto.product.ProductFilter;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.product.ProductService;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController("/search")
@RequiredArgsConstructor
public class PublicProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> searchProducts(
            @RequestBody ProductFilter productFilter,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_PRODUCTS_FILTER.label);
        return productService.getProductsByFilter(productFilter, from, size);
    }
}
