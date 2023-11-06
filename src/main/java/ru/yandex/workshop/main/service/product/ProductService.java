package ru.yandex.workshop.main.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.config.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.exception.AccessDenialException;
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
import ru.yandex.workshop.main.service.image.ImageService;

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
    private final ImageService imageService;

    public List<ProductResponseDto> getProductsSeller(Long sellerId, int from, int size) {
        getSellerFromDatabase(sellerId);
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        // TODO у меня в Postman валится этот метод, Павел Михайлов, проверь пожалуйста
        return productRepository.findProductBySellerId(sellerId, pageRequest)
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long sellerId, Long productId) {
        checkSellerAccessRights(sellerId, productId);
        Product product = getProductFromDatabase(productId);
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto createProduct(ProductDto productDto) {
        if (productDto.getInstallation() && productDto.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо ввести цену устанвоки");
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        product.setCategory(getCategoryFromDatabase(product.getCategory().getId()));
        product.setVendor(getVendorFromDatabase(product.getVendor().getId()));
        product.setSeller(getSellerFromDatabase(product.getSeller().getId()));

        product.setProductStatus(ProductStatus.DRAFT);
        if (product.getQuantity() > 0) {
            product.setProductAvailability(true);
        }
        productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long sellerId, Long productId, ProductDto productDto) {
        checkSellerAccessRights(sellerId, productId);
        Product product = getProductFromDatabase(productId);
        if (productDto.getInstallation() && productDto.getInstallationPrice() == null)
            throw new WrongConditionException("Необходимо ввести цену устанвоки");
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getVersion() != null) {
            product.setVersion(productDto.getVersion());
        }
        if (productDto.getCategory() != null) {
            product.setCategory(Category.builder().id(productDto.getCategory()).build());
        }
        if (productDto.getLicense() != null) {
            product.setLicense(productDto.getLicense());
        }
        if (productDto.getVendor() != null) {
            product.setVendor(Vendor.builder().id(productDto.getVendor()).build());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getQuantity() != null) {
            product.setQuantity(productDto.getQuantity());
            if (product.getQuantity() > 0) {
                product.setProductAvailability(true);
            }
        }
        if (productDto.getInstallation() != null) {
            product.setInstallation(productDto.getInstallation());
        }
        if (product.getInstallation() != null) {
            product.setInstallation(productDto.getInstallation());
        }
        if (productDto.getInstallationPrice() != null) {
            product.setInstallationPrice(productDto.getInstallationPrice());
        }
        product.setProductStatus(ProductStatus.DRAFT);
        productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto createProductImage(Long sellerId, Long productId, MultipartFile file) {
        checkSellerAccessRights(sellerId, productId);
        Product product = getProductFromDatabase(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        product.setImage(ImageMapper.INSTANCE.imageDtoToImage(imageDto));
        return ProductMapper.INSTANCE.productToProductResponseDto(product);
    }

    @Transactional
    public void deleteProductImage(Long sellerId, Long productId) {
        checkSellerAccessRights(sellerId, productId);
        Product product = getProductFromDatabase(productId);
        if (product.getImage() != null) {
            imageService.deleteImageById(product.getImage().getId());
        }
    }

    @Transactional
    public ProductResponseDto updateStatusProductOnSent(Long sellerId, Long productId) {
        checkSellerAccessRights(sellerId, productId);
        Product product = getProductFromDatabase(productId);
        product.setProductStatus(ProductStatus.SHIPPED);
        return ProductMapper.INSTANCE.productToProductResponseDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAllProductsSeller(int from, int size) {
        return productRepository.findAllBy(PageRequestOverride.of(from, size))
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getAllProductsShipped(int from, int size) {
        return productRepository.findAllByProductStatusOrderByProductionTimeDesc(ProductStatus.SHIPPED, PageRequestOverride.of(from, size))
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

    private void checkSellerAccessRights(Long sellerId, Long productId) {
        Product product = getProductFromDatabase(productId);
        Seller seller = getSellerFromDatabase(sellerId);

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new AccessDenialException(String.format(
                    "Продавец %s не может корректировать чужой продукт!",
                    seller.getName()));
        }
    }
}


