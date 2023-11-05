package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductForUpdate;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.CategoryRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;
import ru.yandex.workshop.security.model.user.Seller;
import ru.yandex.workshop.security.repository.SellerRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Transactional
    public ProductResponseDto createProduct(String sellerEmail, ProductDto productDto) {
        Seller seller = getSeller(sellerEmail);
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        product.setSeller(seller);
        product.setProductStatus(ProductStatus.DRAFT);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateProduct(String email, Long productId, ProductForUpdate productForUpdate) {
        Product product = getProductFromDatabase(productId);
        Seller seller = getSeller(email);

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new EntityNotFoundException(ExceptionMessage.NO_RIGHTS_EXCEPTION.label);
        }
        if (productForUpdate.getName() != null) {
            product.setName(productForUpdate.getName());
        }
        if (productForUpdate.getDescription() != null) {
            product.setDescription(productForUpdate.getDescription());
        }
        if (productForUpdate.getVersion() != null) {
            product.setVersion(productForUpdate.getVersion());
        }
        if (productForUpdate.getImage() != null) {
            //TODO сделайте что-нибудь с изображением)
        }
        if (productForUpdate.getCategory() != null) {
            product.setCategory(productForUpdate.getCategory());
        }
        if (productForUpdate.getLicense() != null) {
            product.setLicense(productForUpdate.getLicense());
        }
        if (productForUpdate.getVendor() != null) {
            product.setVendor(productForUpdate.getVendor());
        }
        if (productForUpdate.getPrice() != null) {
            product.setPrice(productForUpdate.getPrice());
        }
        if (productForUpdate.getQuantity() != null) {
            product.setQuantity(productForUpdate.getQuantity());
            if (productForUpdate.getQuantity() > 0) {
                product.setProductAvailability(true);
            }
        }
        if (productForUpdate.getInstallation() != null) {
            product.setInstallation(productForUpdate.getInstallation());
        }
        product.setProductStatus(ProductStatus.DRAFT);
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateStatusProductOnSent(String email, Long productId) {
        Seller seller = getSeller(email);
        Product product = getProductFromDatabase(productId);

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new EntityNotFoundException(ExceptionMessage.NO_RIGHTS_EXCEPTION.label);
        }
        product.setProductStatus(ProductStatus.SHIPPED);
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateStatusProduct(Long productId, ProductStatus status) {
        Product product = getProductFromDatabase(productId);

        switch (status) {
            case PUBLISHED:
                product.setProductStatus(ProductStatus.PUBLISHED);
                product.setProductionTime(LocalDateTime.now());
                break;
            case REJECTED:
                product.setProductStatus(ProductStatus.REJECTED);
                break;
        }

        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        getProductFromDatabase(productId);
        productRepository.deleteById(productId);
    }

    private Product getProductFromDatabase(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private Category getCategoryFromDatabase(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private Vendor getVendorFromDatabase(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private Seller getSeller(String email) {
        return sellerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}


