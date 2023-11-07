package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PublicProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public List<ProductResponseDto> getProductsOfSeller(Long sellerId, int from, int size) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        // TODO у меня в Postman валится этот метод, Павел Михайлов, проверь пожалуйста
        return productRepository.findProductBySellerId(sellerId, pageRequest)
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long productId) {
        return ProductMapper.INSTANCE.productToProductResponseDto(getProductFromDatabase(productId));
    }

    private Product getProductFromDatabase(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}


