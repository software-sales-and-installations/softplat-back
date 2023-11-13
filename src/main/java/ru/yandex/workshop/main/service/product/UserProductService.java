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
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.main.service.seller.SellerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserProductService {

    private final ProductRepository productRepository;
    private final SellerService sellerService;
    private final ImageService imageService;

    public Product createProduct(String sellerEmail, Product product) {
        Seller seller = sellerService.getSeller(sellerEmail);
        product.setSeller(seller);
        product.setProductStatus(ProductStatus.DRAFT);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        if (product.getInstallation() != null && product.getInstallation()
                && product.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо указать цену установки.");
        return productRepository.save(product);
    }

    public Product updateProduct(String email, Long productId, Product productForUpdate) {
        Product product = getProductFromDatabase(productId);
        Seller seller = sellerService.getSeller(email);

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

    public Product createProductImage(String sellerEmail, Long productId, MultipartFile file) {
        checkSellerAccessRights(sellerEmail, productId);
        Product product = getProductFromDatabase(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        product.setImage(imageService.getImageById(imageDto.getId()));
        return product;
    }

    public void deleteProductImageSeller(String sellerEmail, Long productId) {
        checkSellerAccessRights(sellerEmail, productId);
        deleteProductImage(productId);
    }

    public void deleteProductImage(Long productId) {
        Product product = getProductFromDatabase(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
    }

    public Product updateStatusProductOnSent(String email, Long productId) {
        Seller seller = sellerService.getSeller(email);
        Product product = getProductFromDatabase(productId);

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new EntityNotFoundException(ExceptionMessage.NO_RIGHTS_EXCEPTION.label);
        }
        product.setProductStatus(ProductStatus.SHIPPED);
        return productRepository.save(product);
    }

    public Product updateStatusProduct(Long productId, ProductStatus status) {
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

        return productRepository.save(product);
    }

    public void deleteProductSeller(String sellerEmail, Long productId) {
        checkSellerAccessRights(sellerEmail, productId);
        deleteProduct(productId);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsShipped(int from, int size) {
        return new ArrayList<>(productRepository.findAllByProductStatusOrderByProductionTimeDesc(ProductStatus.SHIPPED,
                PageRequestOverride.of(from, size)));
    }

    public void deleteProduct(Long productId) {
        getProductFromDatabase(productId);
        productRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public Product getProductFromDatabase(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private void checkSellerAccessRights(String email, Long productId) {
        Product product = getProductFromDatabase(productId);
        Seller seller = sellerService.getSeller(email);

        if (!product.getSeller().getEmail().equals(email)) {
            throw new AccessDenialException(String.format(
                    "Продавец %s не может корректировать чужой продукт!",
                    seller.getName()));
        }
    }
}