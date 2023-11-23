package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorsListResponseDto;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.util.List;

@Mapper(uses = ImageMapper.class)
@Component
public interface VendorMapper {

    Vendor vendorDtoToVendor(VendorCreateUpdateDto vendorCreateUpdateDto);

    VendorResponseDto vendorToVendorResponseDto(Vendor vendor);

    default VendorsListResponseDto toVendorsListResponseDto(List<VendorResponseDto> vendors) {
        return VendorsListResponseDto.builder().vendors(vendors).build();
    }
}
