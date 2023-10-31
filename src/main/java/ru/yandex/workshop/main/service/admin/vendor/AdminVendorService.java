package ru.yandex.workshop.main.service.admin.vendor;

import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;

import java.util.List;

public interface AdminVendorService {
    VendorResponseDto createVendor(VendorDto vendorDto);

    public VendorResponseDto changeVendorById(Long vendorId, VendorDto vendorDto);

    List<VendorResponseDto> findVendorAll();

    VendorResponseDto findVendorById(Long vendorId);

    void deleteVendor(Long vendorId);
}
