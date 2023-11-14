package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.util.List;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
@Component
public interface VendorMapper {

    Vendor vendorDtoToVendor(VendorDto vendorDto);

    @Mapping(target = "imageResponseDto", source = "image")
    VendorResponseDto vendorToVendorResponseDto(Vendor vendor);

    List<VendorResponseDto> vendorToListVendorResponseDto(List<Vendor> vendor);

}
