package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.main.dto.vendor.VendorResponseDto;
import ru.softplat.main.dto.vendor.VendorsListResponseDto;
import ru.softplat.main.server.model.vendor.Vendor;

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
