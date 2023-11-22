package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.product.ProductCreateUpdateDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsListResponseDto;
import ru.yandex.workshop.main.model.product.Product;

import java.util.List;

@Mapper(uses = {ImageMapper.class, SellerMapper.class, VendorMapper.class})
@Component
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "vendor", ignore = true)
    Product productDtoToProduct(ProductCreateUpdateDto productCreateUpdateDto);

    ProductResponseDto productToProductResponseDto(Product product);

    default ProductsListResponseDto toProductsListResponseDto(List<ProductResponseDto> products) {
        return ProductsListResponseDto.builder().products(products).build();
    }
}
