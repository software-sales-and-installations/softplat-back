package ru.yandex.workshop.main.dto.vendor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.util.List;

@Mapper
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    Vendor vendorDtoToVendor(VendorDto vendorDto);

    @Mapping(target = "imageResponseDto", expression = "java(mapImageToImageResponseDto(vendor))")
    VendorResponseDto vendorToVendorResponseDto(Vendor vendor);

    List<VendorResponseDto> vendorToListVendorResponseDto(List<Vendor> vendor);

    default ImageResponseDto mapImageToImageResponseDto(Vendor vendor) {
        Image image = vendor.getImage();
        return ImageMapper.INSTANCE.imageToImageResponseDto(image);
    }
}
