package ru.softplat.main.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.category.CategoryResponseDto;
import ru.softplat.main.dto.image.ImageResponseDto;
import ru.softplat.main.dto.user.response.SellerResponseDto;
import ru.softplat.main.dto.vendor.VendorResponseDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto {
    Long id;
    String name;
    String description;
    String version;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime productionTime;
    ImageResponseDto image;
    CategoryResponseDto category;
    Boolean hasDemo;
    VendorResponseDto vendor;
    SellerResponseDto seller;
    Float price;
    Integer quantity;
    Boolean installation;
    ProductStatus productStatus;
    Boolean productAvailability;
    Float installationPrice;
}