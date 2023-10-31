package ru.yandex.workshop.main.service.admin.vendor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorMapper;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorUpdateDto;
import ru.yandex.workshop.main.exception.VendorNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final VendorRepository repository;

    @Override
    public VendorResponseDto createVendor(VendorDto vendorDto) {
        log.debug(LogMessage.ADMIN_ADD_VENDOR.label);
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(repository.save(VendorMapper.INSTANCE.vendorDtoToVendor(vendorDto)));
    }

    @Override
    public VendorResponseDto changeVendorById(Long vendorId, VendorUpdateDto vendorUpdateDto) {
        Vendor oldVendor = availabilityVendor(vendorId);

        if (vendorUpdateDto.getName() != null) {
            oldVendor.setName(vendorUpdateDto.getName());
        }
        if (vendorUpdateDto.getDescription() != null) {
            oldVendor.setDescription(vendorUpdateDto.getDescription());
        }
        if (vendorUpdateDto.getImageId() != null) {
            oldVendor.setImageId(vendorUpdateDto.getImageId());
        }
        if (vendorUpdateDto.getCountry() != null) {
            oldVendor.setCountry(vendorUpdateDto.getCountry());
        }

        log.debug(LogMessage.ADMIN_PATCH_VENDOR.label);
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(repository.save(oldVendor));
    }

    @Transactional(readOnly = true)
    @Override
    public List<VendorResponseDto> findVendorAll() {
        log.debug(LogMessage.ADMIN_GET_VENDOR.label);
        return VendorMapper.INSTANCE.vendorToListVendorResponseDto(repository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public VendorResponseDto findVendorById(Long vendorId) {
        log.debug(LogMessage.ADMIN_GET_ID_VENDOR.label);
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(repository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException(ExceptionMessage.NOT_FOUND_VENDOR_EXCEPTION.label)));
    }

    @Override
    public void deleteVendor(Long vendorId) {
        availabilityVendor(vendorId);
        log.debug(LogMessage.ADMIN_DELETE_VENDOR.label);
        repository.deleteById(vendorId);
    }

    private Vendor availabilityVendor(Long vendorId){
        return repository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException(ExceptionMessage.NOT_FOUND_VENDOR_EXCEPTION.label));
    }
}