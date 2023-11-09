package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.exception.AccessDenialException;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.service.image.ImageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final ImageService imageService;

    @Transactional
    public ProductResponseDto createProduct(String sellerEmail, ProductDto productDto) {
        Seller seller = getSeller(sellerEmail);
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        product.setSeller(seller);
        product.setProductStatus(ProductStatus.DRAFT);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        if (productDto.getInstallation() != null && productDto.getInstallation()
                && productDto.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо указать цену установки.");
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateProduct(String email, Long productId, ProductDto productForUpdate) {
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
        if (productForUpdate.getCategory() != null) {
            product.setCategory(Category.builder().id(productForUpdate.getCategory()).build());
        }
        if (productForUpdate.getLicense() != null) {
            product.setLicense(productForUpdate.getLicense());
        }
        if (productForUpdate.getVendor() != null) {
            product.setVendor(Vendor.builder().id(productForUpdate.getVendor()).build());
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
        if (productForUpdate.getInstallation() != null && productForUpdate.getInstallation()
                && productForUpdate.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо указать цену установки.");
        product.setProductStatus(ProductStatus.DRAFT);
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto createProductImage(String sellerEmail, Long productId, MultipartFile file) {
        checkSellerAccessRights(sellerEmail, productId);
        Product product = getProductFromDatabase(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        product.setImage(ImageMapper.INSTANCE.imageDtoToImage(imageDto));
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public void deleteProductImageSeller(String sellerEmail, Long productId) {
        checkSellerAccessRights(sellerEmail, productId);
        deleteProductImage(productId);
    }

    @Transactional
    public void deleteProductImage(Long productId) {
        Product product = getProductFromDatabase(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
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

        if (product.getProductStatus() == status)
            throw new DuplicateException("Продукт уже имеет этот статус.");

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
    public void deleteProductSeller(String sellerEmail, Long productId) {
        checkSellerAccessRights(sellerEmail, productId);
        deleteProduct(productId);
    }


    public List<ProductResponseDto> getAllProductsShipped(int from, int size) {
        return productRepository.findAllByProductStatusOrderByProductionTimeDesc(ProductStatus.SHIPPED,
                        PageRequestOverride.of(from, size))
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
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

    private Seller getSeller(String email) {
        return sellerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private void checkSellerAccessRights(String email, Long productId) {
        Product product = getProductFromDatabase(productId);
        Seller seller = getSeller(email);

        if (!product.getSeller().getEmail().equals(email)) {
            throw new AccessDenialException(String.format(
                    "Продавец %s не может корректировать чужой продукт!",
                    seller.getName()));
        }
    }
}


