package ru.yandex.workshop.main.service.vendor;

import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorSearchRequestDto;
import ru.yandex.workshop.main.model.vendor.Vendor;

import java.util.List;

public interface VendorService {
    Vendor createVendor(VendorDto vendorDto);

    Vendor changeVendorById(Long vendorId, VendorDto vendorUpdateDto);

    List<Vendor> findVendorsWithFilter(VendorSearchRequestDto vendorSearchRequestDto, int from, int size);

    Vendor getVendorById(Long vendorId);

    void deleteVendor(Long vendorId);

    Vendor addVendorImage(Long vendorId, MultipartFile file);

    void deleteVendorImage(Long vendorId);
}
