package ru.yandex.workshop.main.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.dto.product.ProductFilter;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.service.product.ProductService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicProductController {

    private final ProductService productService;

    @GetMapping(path = "/search")
    public List<ProductResponseDto> searchProducts(@RequestBody ProductFilter productFilter) {
        return productService.getProductsByFilter(productFilter);
    }

}
