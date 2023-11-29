package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.dto.product.ProductCreateUpdateDto;
import ru.softplat.dto.product.ProductResponseDto;
import ru.softplat.dto.product.ProductsListResponseDto;

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
