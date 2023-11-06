package ru.yandex.workshop.main.service.vendor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorMapper;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;
import ru.yandex.workshop.main.service.image.ImageService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final VendorRepository repository;
    private final ImageService imageService;

    @Override
    public VendorResponseDto createVendor(VendorDto vendorDto) {
        return VendorMapper.INSTANCE
                .vendorToVendorResponseDto(repository
                        .save(VendorMapper.INSTANCE
                                .vendorDtoToVendor(vendorDto)));
    }

    @Override
    public VendorResponseDto changeVendorById(Long vendorId, VendorDto vendorUpdateDto) {
        Vendor oldVendor = availabilityVendor(vendorId);

        if (vendorUpdateDto.getName() != null) {
            oldVendor.setName(vendorUpdateDto.getName());
        }
        if (vendorUpdateDto.getDescription() != null) {
            oldVendor.setDescription(vendorUpdateDto.getDescription());
        }
        if (vendorUpdateDto.getCountry() != null) {
            oldVendor.setCountry(vendorUpdateDto.getCountry());
        }

        return VendorMapper.INSTANCE.vendorToVendorResponseDto(repository.save(oldVendor));
    }

    @Transactional(readOnly = true)
    @Override
    public List<VendorResponseDto> findVendorAll() {
        return VendorMapper.INSTANCE
                .vendorToListVendorResponseDto(repository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public VendorResponseDto findVendorById(Long vendorId) {
        return VendorMapper.INSTANCE
                .vendorToVendorResponseDto(repository.findById(vendorId)
                        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    @Override
    public void deleteVendor(Long vendorId) {
        availabilityVendor(vendorId);
        repository.deleteById(vendorId);
    }

    @Override
    public VendorResponseDto addVendorImage(Long vendorId, MultipartFile file) {
        Vendor vendor = availabilityVendor(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        vendor.setImage(ImageMapper.INSTANCE.imageDtoToImage(imageDto));
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(vendor);
    }

    @Override
    public void deleteVendorImage(Long vendorId) {
        Vendor vendor = availabilityVendor(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
    }

    private Vendor availabilityVendor(Long vendorId) {
        return repository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}