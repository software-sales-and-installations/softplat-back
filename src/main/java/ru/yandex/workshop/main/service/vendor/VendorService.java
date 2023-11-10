package ru.yandex.workshop.main.service.vendor;

import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorFilter;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;

import java.util.List;

public interface VendorService {
    VendorResponseDto createVendor(VendorDto vendorDto);

    VendorResponseDto changeVendorById(Long vendorId, VendorDto vendorUpdateDto);

    List<VendorResponseDto> findVendorAll(VendorFilter vendorFilter, int from, int size);

    VendorResponseDto findVendorById(Long vendorId);

    void deleteVendor(Long vendorId);

    VendorResponseDto addVendorImage(Long vendorId, MultipartFile file);

    void deleteVendorImage(Long vendorId);
}
