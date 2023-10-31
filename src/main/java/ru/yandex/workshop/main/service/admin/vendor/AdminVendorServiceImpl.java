package ru.yandex.workshop.main.service.admin.vendor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorMapper;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.exception.VendorNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;

import java.util.List;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class AdminVendorServiceImpl implements AdminVendorService {
    @Autowired
    private VendorRepository repository;

    @Override
    public VendorResponseDto createVendor(VendorDto vendorDto) {
        log.debug(LogMessage.ADMIN_ADD_VENDOR.label);
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(repository.save(VendorMapper.INSTANCE.vendorDtoToVendor(vendorDto)));
    }

    @Override
    public VendorResponseDto changeVendorById(Long vendorId, VendorDto vendorDto) {
        Vendor oldVendor = repository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException(ExceptionMessage.NOT_FOUND_VENDOR_EXCEPTION.label));

        if (vendorDto.getName() != null) {
            oldVendor.setName(vendorDto.getName());
        }
        if (vendorDto.getDescription() != null) {
            oldVendor.setDescription(vendorDto.getDescription());
        }
        if (vendorDto.getImageId() != null) {
            oldVendor.setImageId(vendorDto.getImageId());
        }
        if (vendorDto.getCountry() != null) {
            oldVendor.setCountry(vendorDto.getCountry());
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
        log.debug(LogMessage.ADMIN_DELETE_VENDOR.label);
        repository.deleteById(vendorId);
    }
}