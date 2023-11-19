package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.model.vendor.Vendor;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
@Component
public interface VendorMapper {

    Vendor vendorDtoToVendor(VendorCreateUpdateDto vendorCreateUpdateDto);

    @Mapping(target = "imageResponseDto", source = "image")
    VendorResponseDto vendorToVendorResponseDto(Vendor vendor);
}
