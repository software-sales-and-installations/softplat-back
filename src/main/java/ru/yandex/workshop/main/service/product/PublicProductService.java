package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.License;
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

    public List<ProductResponseDto> getProductsAll(int from, int size) {
        Pageable page = PageRequest.of(from, size);

        return productRepository.findAllProducts(page)
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long productId, License license) {
        if (license != null) {
            Product product = getProductFromDatabase(productId);
            return ProductMapper.INSTANCE.productToProductResponseDto(getProductFromDatabaseByLicense(product.getName(), license));
        }

        return ProductMapper.INSTANCE.productToProductResponseDto(getProductFromDatabase(productId));
    }


    private Product getProductFromDatabase(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private Product getProductFromDatabaseByLicense(String name, License license) {
        return productRepository.findProductByNameEqualsAndLicenseEquals(name, license)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}


