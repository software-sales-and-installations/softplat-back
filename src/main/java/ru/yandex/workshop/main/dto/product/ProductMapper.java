package ru.yandex.workshop.main.dto.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.dto.seller.SellerMapper;
import ru.yandex.workshop.main.dto.seller.SellerResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorMapper;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "category", expression = "java(mapCategoryIdToCategory(productDto))")
    @Mapping(target = "vendor", expression = "java(mapVendorIdToVendor(productDto))")
    @Mapping(target = "seller", expression = "java(mapSellerIdToSeller(productDto))")
    Product productDtoToProduct(ProductDto productDto);

//    @Mapping(target = "imageResponseDto", expression = "java(mapImageToImageResponseDto(product))")
//    ProductDto productToProductDto(Product product);

//    ProductForUpdate productToProductForUpdate(Product product);

    @Mapping(target = "image", expression = "java(mapImageToImageResponseDto(product))")
    @Mapping(target = "vendor", expression = "java(mapVendorToVendorResponseDto(product))")
    @Mapping(target = "seller", expression = "java(mapSellerToSellerResponseDto(product))")
    ProductResponseDto productToProductResponseDto(Product product);

    default ImageResponseDto mapImageToImageResponseDto(Product product) {
        Image image = product.getImage();
        return ImageMapper.INSTANCE.imageToImageResponseDto(image);
    }

    default VendorResponseDto mapVendorToVendorResponseDto(Product product) {
        Vendor vendor = product.getVendor();
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(vendor);
    }

    default SellerResponseDto mapSellerToSellerResponseDto(Product product) {
        Seller seller = product.getSeller();
        return SellerMapper.INSTANCE.sellerToSellerForResponse(seller);
    }

    default Category mapCategoryIdToCategory(ProductDto productDto) {
        long categoryId = productDto.getCategory();
        return Category.builder().id(categoryId).build();
    }

    default Vendor mapVendorIdToVendor(ProductDto productDto) {
        long vendorId = productDto.getVendor();
        return Vendor.builder().id(vendorId).build();
    }

    default Seller mapSellerIdToSeller(ProductDto productDto) {
        long sellerId = productDto.getSeller();
        return Seller.builder().id(sellerId).build();
    }
}
