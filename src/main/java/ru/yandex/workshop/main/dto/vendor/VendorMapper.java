package ru.yandex.workshop.main.dto.vendor;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.util.List;

@Mapper
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    Vendor vendorDtoToVendor(VendorDto vendorDto);

    VendorResponseDto vendorToVendorResponseDto(Vendor vendor);

    List<VendorResponseDto> vendorToListVendorResponseDto(List<Vendor> vendor);
}
