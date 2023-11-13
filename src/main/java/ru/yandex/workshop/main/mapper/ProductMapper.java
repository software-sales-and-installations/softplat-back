package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.vendor.Vendor;

@Mapper(componentModel = "spring", uses = {ImageMapper.class, SellerMapper.class, VendorMapper.class})
@Component
public interface ProductMapper {

    @Mapping(target = "category", expression = "java(mapCategoryIdToCategory(productDto))")
    @Mapping(target = "vendor", expression = "java(mapVendorIdToVendor(productDto))")
    Product productDtoToProduct(ProductDto productDto);

    ProductResponseDto productToProductResponseDto(Product product);

    default Category mapCategoryIdToCategory(ProductDto productDto) {
        long categoryId = productDto.getCategory();
        return Category.builder().id(categoryId).build();
    }

    default Vendor mapVendorIdToVendor(ProductDto productDto) {
        long vendorId = productDto.getVendor();
        return Vendor.builder().id(vendorId).build();
    }
}
