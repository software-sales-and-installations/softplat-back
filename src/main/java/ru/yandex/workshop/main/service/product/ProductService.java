package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.config.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductForUpdate;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.CategoryRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public List<ProductResponseDto> getProductsSeller(Long sellerId, int from, int size) {
        getSellerFromDatabase(sellerId);
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        return productRepository.findProductBySellerId(sellerId, pageRequest)
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long sellerId, Long productId) {
        Seller seller = getSellerFromDatabase(sellerId);
        Product product = getProductFromDatabase(productId);
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new EntityNotFoundException(String.format(
                    "Продавец %s не может посмотреть чужой продукт!",
                    seller.getName()));
        }
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto createProduct(ProductDto productDto) {
        if (productDto.getInstallation() && productDto.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо ввести цену устанвоки");
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        getCategoryFromDatabase(product.getCategory().getId());
        getVendorFromDatabase(product.getVendor().getId());
        getSellerFromDatabase(product.getSeller().getId());
        product.setProductStatus(ProductStatus.DRAFT);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long sellerId, Long productId, ProductForUpdate productForUpdate) {
        Product product = getProductFromDatabase(productId);
        Seller seller = getSellerFromDatabase(sellerId);

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new EntityNotFoundException(String.format(
                    "Продавец %s не может корректировать чужой продукт!",
                    seller.getName()));
        }
        if (productForUpdate.getInstallation() && productForUpdate.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо ввести цену устанвоки");
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
        }
        if (productForUpdate.getInstallation() != null) {
            product.setInstallation(productForUpdate.getInstallation());
        }
        if (productForUpdate.getInstallationPrice() != null) {
            product.setInstallationPrice(productForUpdate.getInstallationPrice());
        }
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        product.setProductStatus(ProductStatus.DRAFT);
        productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateStatusProductOnSent(Long sellerId, Long productId) {
        Seller seller = getSellerFromDatabase(sellerId);
        Product product = getProductFromDatabase(productId);

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new EntityNotFoundException(String.format(
                    "Продавец %s не может корректировать чужой продукт!",
                    seller.getName()));
        }
        product.setProductStatus(ProductStatus.SHIPPED);
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAllProductsSeller(int from, int size) {
        return productRepository.findAllBy(PageRequestOverride.of(from, size))
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductByIdAdmin(Long productId) {
        return ProductMapper.INSTANCE.productToProductResponseDto(getProductFromDatabase(productId));
    }

    @Transactional
    public ProductResponseDto updateStatusProductOnPublished(Long productId) {
        Product product = getProductFromDatabase(productId);
        product.setProductStatus(ProductStatus.PUBLISHED);
        product.setProductionTime(LocalDateTime.now());
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateStatusProductOnRejected(Long productId) {
        Product product = getProductFromDatabase(productId);
        product.setProductStatus(ProductStatus.REJECTED);
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProductAdmin(Long productId) {
        getProductFromDatabase(productId);
        productRepository.deleteById(productId);
    }

    private Product getProductFromDatabase(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Такого продукта не существует"));
    }

    private Category getCategoryFromDatabase(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Такой категории не существует"));
    }

    private Vendor getVendorFromDatabase(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException("Такого производителя не существует"));
    }

    private Seller getSellerFromDatabase(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Такого пользователя не существует"));
    }
}


