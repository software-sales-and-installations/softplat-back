package ru.softplat.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.configuration.PageRequestOverride;
import ru.softplat.exception.AccessDenialException;
import ru.softplat.exception.DuplicateException;
import ru.softplat.exception.EntityNotFoundException;
import ru.softplat.exception.WrongConditionException;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.model.product.Category;
import ru.softplat.model.product.Product;
import ru.softplat.model.product.ProductStatus;
import ru.softplat.model.vendor.Vendor;
import ru.softplat.repository.product.ProductRepository;
import ru.softplat.service.image.ImageService;
import ru.softplat.service.seller.SellerService;


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

    public Product create(String sellerEmail, Product product, Long categoryId, Long vendorId) {
        initProduct(sellerEmail, product, categoryId, vendorId);
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
            Category category = categoryService.getCategoryById(productForUpdate.getCategory().getId());
            product.setCategory(category);
        }
        if (productForUpdate.getLicense() != null) {
            product.setLicense(productForUpdate.getLicense());
        }
        if (productForUpdate.getVendor() != null) {
            Vendor vendor = vendorService.getVendorById(productForUpdate.getVendor().getId());
            product.setVendor(vendor);
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

    public void updateProductQuantityWhenOrder(Long productId, Integer orderQuantity) {
        Product product = getProductOrThrowException(productId);
        product.setQuantity(product.getQuantity() - orderQuantity);
        productRepository.save(product);
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
        return productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(productId), Product.class)
                ));
    }

    private void initProduct(String sellerEmail, Product product, long categoryId, long vendorId) {
        Seller seller = sellerService.getSeller(sellerEmail);
        Category category = categoryService.getCategoryById(categoryId);
        Vendor vendor = vendorService.getVendorById(vendorId);

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

    @Transactional(readOnly = true)
    public Product getAvailableProduct(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(productId), Product.class)
                ));
        if (!product.getProductAvailability() || product.getProductStatus() != ProductStatus.PUBLISHED)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(productId), Product.class)
            );
        return product;
    }
}