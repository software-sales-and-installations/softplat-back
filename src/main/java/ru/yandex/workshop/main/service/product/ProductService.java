package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.exception.AccessDenialException;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.main.service.seller.SellerService;
import ru.yandex.workshop.main.service.vendor.VendorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerService sellerService;
    private final CategoryService categoryService;
    private final VendorService vendorService;
    private final ImageService imageService;

    public Product create(String sellerEmail, Product product) {
        initProduct(sellerEmail, product);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        if (product.getInstallation() != null && product.getInstallation()
                && product.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо указать цену установки.");

        return productRepository.save(product);
    }

    public Product update(Long productId, Product productForUpdate) {
        Product product = getProductOrThrowException(productId);

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
            product.setCategory(Category.builder().id(productForUpdate.getCategory().getId()).build());
        }
        if (productForUpdate.getLicense() != null) {
            product.setLicense(productForUpdate.getLicense());
        }
        if (productForUpdate.getVendor() != null) {
            product.setVendor(Vendor.builder().id(productForUpdate.getVendor().getId()).build());
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

        return productRepository.save(product);
    }

    public Product createProductImage(Long productId, MultipartFile file) {
        Product product = getProductOrThrowException(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
        product.setImage(imageService.addNewImage(file));
        return product;
    }

    public void deleteProductImage(Long productId) {
        Product product = getProductOrThrowException(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
    }

    public Product updateStatus(Long productId, ProductStatus status) {
        Product product = getProductOrThrowException(productId);

        if (product.getProductStatus() == status)
            throw new DuplicateException("Продукт уже имеет этот статус.");

        switch (status) {
            case PUBLISHED:
                if (product.getProductStatus() != ProductStatus.SHIPPED)
                    throw new WrongConditionException("Продукт не подлежит модерации");
                product.setProductStatus(ProductStatus.PUBLISHED);
                product.setProductionTime(LocalDateTime.now());
                break;
            case REJECTED:
                if (product.getProductStatus() == ProductStatus.DRAFT)
                    throw new WrongConditionException("Продукт не подлежит модерации");
                product.setProductStatus(ProductStatus.REJECTED);
                break;
            case SHIPPED:
                product.setProductStatus(ProductStatus.SHIPPED);
                break;
        }

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsShipped(int from, int size) {
        return productRepository.findAllByProductStatusOrderByProductionTimeDesc(ProductStatus.SHIPPED,
                PageRequestOverride.of(from, size));
    }

    public void delete(Long productId) {
        getProductOrThrowException(productId);
        productRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public Product getProductOrThrowException(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private void initProduct(String sellerEmail, Product product) {
        Seller seller = sellerService.getSeller(sellerEmail);
        Category category = categoryService.getCategoryById(product.getCategory().getId());
        Vendor vendor = vendorService.getVendorById(product.getVendor().getId());

        product.setSeller(seller);
        product.setCategory(category);
        product.setVendor(vendor);
        product.setProductStatus(ProductStatus.DRAFT);
    }

    public void checkSellerAccessRights(String email, Long productId) {
        Product product = getProductOrThrowException(productId);

        if (!product.getSeller().getEmail().equals(email)) {
            throw new AccessDenialException(ExceptionMessage.NO_RIGHTS_EXCEPTION.label);
        }
    }

    public boolean checkExistsProduct(Long productId) {
        return productRepository.existsById(productId);
    }
}