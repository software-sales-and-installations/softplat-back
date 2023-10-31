package ru.yandex.workshop.main.service.admin.vendor;

import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorUpdateDto;

import java.util.List;

public interface VendorService {
    VendorResponseDto createVendor(VendorDto vendorDto);

    VendorResponseDto changeVendorById(Long vendorId, VendorUpdateDto vendorUpdateDto);

    List<VendorResponseDto> findVendorAll();

    VendorResponseDto findVendorById(Long vendorId);

    void deleteVendor(Long vendorId);
}
