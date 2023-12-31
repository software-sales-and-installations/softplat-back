package ru.softplat.main.server.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.AccessDenialException;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.StatsMapper;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.image.Image;
import ru.softplat.main.server.model.product.Category;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.product.ProductList;
import ru.softplat.main.server.model.seller.Seller;
import ru.softplat.main.server.model.vendor.Vendor;
import ru.softplat.main.server.repository.product.ProductRepository;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.image.ImageService;
import ru.softplat.main.server.service.seller.SellerService;
import ru.softplat.main.server.service.vendor.VendorService;
import ru.softplat.stats.client.StatsClient;

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
    private final BuyerService buyerService;
    private final StatsClient statsClient;
    private final StatsMapper statsMapper;

    public Product create(long sellerId, Product product, Long categoryId, Long vendorId) {
        if (product.getInstallation() != null && product.getInstallation()
                && product.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо указать цену установки.");

        initProduct(sellerId, product, categoryId, vendorId);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }

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
        if (productForUpdate.getHasDemo() != null) {
            product.setHasDemo(productForUpdate.getHasDemo());
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

    public Product createProductImage(Long productId, Image image) {
        Product product = getProductOrThrowException(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
        product.setImage(imageService.addNewImage(image));
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
    public ProductList getAllProductsAdminByStatus(int from, int size, ProductStatus status) {
        Page<Product> products = productRepository.findAllByProductStatusOrderByProductionTimeDesc(status,
                PageRequestOverride.of(from, size));
        return ProductList.builder()
                .products(products.getContent())
                .count(productRepository.countAllByProductStatus(status))
                .build();
    }

    @Transactional(readOnly = true)
    public ProductList getAllProductsSellerByStatus(long sellerId, int minId, int pageSize, ProductStatus status) {
        List<Product> products = productRepository.findAllByProductStatusAndSellerIdOrderByProductionTimeDesc(
                status, sellerId, PageRequestOverride.of(minId, pageSize));
        return ProductList.builder()
                .products(products)
                .count(productRepository.countAllByProductStatusAndSellerId(status, sellerId))
                .build();
    }

    public void delete(Long productId) {
        getProductOrThrowException(productId);
        productRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public Product getProductOrThrowException(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(productId, Product.class)
                ));
    }

    private void initProduct(long sellerId, Product product, long categoryId, long vendorId) {
        Seller seller = sellerService.getSeller(sellerId);
        Category category = categoryService.getCategoryById(categoryId);
        Vendor vendor = vendorService.getVendorById(vendorId);

        product.setSeller(seller);
        product.setCategory(category);
        product.setVendor(vendor);
        product.setProductStatus(ProductStatus.DRAFT);
    }

    @Transactional(readOnly = true)
    public void checkSellerAccessRights(long sellerId, Long productId) {
        Product product = getProductOrThrowException(productId);

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new AccessDenialException(ExceptionMessage.NO_RIGHTS_EXCEPTION.label);
        }
    }

    @Transactional(readOnly = true)
    public Product getAvailableProduct(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(productId, Product.class)
                ));
        if (!product.getProductAvailability() || product.getProductStatus() != ProductStatus.PUBLISHED)
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(productId, Product.class)
            );
        return product;
    }

    public void updateProductRatingOnCommentCreate(long productId, float rating, long ratingCount) {
        Product product = getAvailableProduct(productId);
        if (product.getRating() == null || ratingCount == 0) {
            product.setRating(rating);
        } else {
            Float averageRating = product.getRating();
            Float newRating = (ratingCount * averageRating + rating) / (ratingCount + 1);
            product.setRating(newRating);
        }
        productRepository.save(product);
    }

    public void updateProductRatingOnCommentDelete(long productId, float rating, long ratingCount) {
        Product product = getAvailableProduct(productId);
        if (ratingCount == 1) {
            product.setRating(null);
        } else {
            Float averageRating = product.getRating();
            Float newRating = (ratingCount * averageRating - rating) / (ratingCount - 1);
            product.setRating(newRating);
        }
        productRepository.save(product);
    }

    public void updateProductRatingOnPatch(long productId, float ratingUpdate, long ratingCount, float oldRating) {
        Product product = getAvailableProduct(productId);
        Float averageRating = product.getRating();
        Float newRating = (ratingCount * averageRating - oldRating + ratingUpdate) / ratingCount;
        product.setRating(newRating);
        productRepository.save(product);
    }

    public void updateProductComplaintCountOnCreate(long productId) {
        Product product = getProductOrThrowException(productId);

        if (product.getComplaintCount() == null) product.setComplaintCount(1);
        else product.setComplaintCount(product.getComplaintCount() + 1);

        if (product.getComplaintCount() >= 10) {
            product.setProductAvailability(Boolean.FALSE);
            product.setProductStatus(ProductStatus.REJECTED);
        }
        productRepository.save(product);
    }

    public void updateProductComplaintCountOnDelete(long productId) {
        Product product = getProductOrThrowException(productId);

        if (product.getComplaintCount() == null || product.getComplaintCount() == 0) {
            throw new WrongConditionException(ExceptionMessage.WRONG_CONDITION_EXCEPTION.label);
        }
        product.setComplaintCount(product.getComplaintCount() - 1);

        if (product.getComplaintCount() < 1) {
            product.setProductStatus(ProductStatus.PUBLISHED);
        }
        productRepository.save(product);
    }

    public void downloadDemo(long userId, long productId) {
        Product product = getAvailableProduct(productId);
        if (!product.getHasDemo()) throw new WrongConditionException("Скачивание демо не предусмотрено");
        Buyer buyer = buyerService.getBuyer(userId);

        statsClient.addDemoStats(statsMapper.mapToDemoDto(buyer, product));
    }
}