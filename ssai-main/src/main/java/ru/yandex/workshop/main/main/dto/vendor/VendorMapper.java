package ru.yandex.workshop.main.main.dto.vendor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.main.model.vendor.Vendor;

import java.util.List;

@Mapper
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    @Mapping(target = "name", source = "vendorDto.name")
    @Mapping(target = "description", source = "vendorDto.description")
    @Mapping(target = "imageId", source = "vendorDto.imageId") //TODO
    @Mapping(target = "country", source = "vendorDto.country")
    Vendor vendorDtoToVendor(VendorDto vendorDto);

    @Mapping(target = "id", source = "vendor.id")
    @Mapping(target = "name", source = "vendor.name")
    @Mapping(target = "description", source = "vendor.description")
    @Mapping(target = "imageId", source = "vendor.imageId") //TODO
    @Mapping(target = "country", source = "vendor.country")
    VendorResponseDto vendorToVendorResponseDto(Vendor vendor);

    @Mapping(target = "id", source = "vendor.id")
    @Mapping(target = "name", source = "vendor.name")
    @Mapping(target = "description", source = "vendor.description")
    @Mapping(target = "imageId", source = "vendor.imageId") //TODO
    @Mapping(target = "country", source = "vendor.country")
    List<VendorResponseDto> vendorToListVendorResponseDto(List<Vendor> vendor);
}
