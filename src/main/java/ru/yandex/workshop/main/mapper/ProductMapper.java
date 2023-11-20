package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.product.ProductCreateUpdateDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.vendor.Vendor;

@Mapper(componentModel = "spring", uses = {ImageMapper.class, SellerMapper.class, VendorMapper.class})
@Component
public interface ProductMapper {

    @Mapping(target = "category", expression = "java(mapCategoryIdToCategory(productCreateUpdateDto))")
    @Mapping(target = "vendor", expression = "java(mapVendorIdToVendor(productCreateUpdateDto))")
    Product productDtoToProduct(ProductCreateUpdateDto productCreateUpdateDto);

    ProductResponseDto productToProductResponseDto(Product product);

    default Category mapCategoryIdToCategory(ProductCreateUpdateDto productCreateUpdateDto) {
        if (productCreateUpdateDto.getCategory() != null) {
            long categoryId = productCreateUpdateDto.getCategory();
            return Category.builder().id(categoryId).build();
        }
        return null;
    }

    default Vendor mapVendorIdToVendor(ProductCreateUpdateDto productCreateUpdateDto) {
        if (productCreateUpdateDto.getVendor() != null) {
            long vendorId = productCreateUpdateDto.getVendor();
            return Vendor.builder().id(vendorId).build();
        }
        return null;
    }
}
