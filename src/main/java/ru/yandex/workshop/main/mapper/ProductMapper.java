package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.Product;

@Mapper(uses = {ImageMapper.class, SellerMapper.class, VendorMapper.class})
@Component
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "vendor", ignore = true)
    Product productDtoToProduct(ProductDto productDto);

    ProductResponseDto productToProductResponseDto(Product product);

}
