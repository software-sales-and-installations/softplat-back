package ru.yandex.workshop.main.service.vendor;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.config.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorFilter;
import ru.yandex.workshop.main.dto.vendor.VendorMapper;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.vendor.QVendor;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.main.util.QPredicates;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final VendorRepository repository;
    private final ImageService imageService;

    @Transactional
    @Override
    public VendorResponseDto createVendor(VendorDto vendorDto) {
        return VendorMapper.INSTANCE
                .vendorToVendorResponseDto(repository.save(VendorMapper.INSTANCE.vendorDtoToVendor(vendorDto)));
    }

    @Transactional
    @Override
    public VendorResponseDto changeVendorById(Long vendorId, VendorDto vendorUpdateDto) {
        Vendor oldVendor = getVendor(vendorId);

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

    @Override
    public List<VendorResponseDto> findVendorAll(VendorFilter vendorFilter, int from, int size) {
        PageRequest pageRequest = PageRequestOverride.of(from, size);

        QVendor vendor = QVendor.vendor;

        Function<String, Predicate> textPredicateFunction = text -> {
            BooleanExpression nameExpression = vendor.name.toLowerCase().like("%" + text.toLowerCase() + "%");
            BooleanExpression descriptionExpression = vendor.description.toLowerCase().like("%" + text.toLowerCase() + "%");
            return nameExpression.or(descriptionExpression);
        };

        Predicate predicate = QPredicates.builder()
                .add(vendorFilter.getText(), textPredicateFunction)
                .add(vendorFilter.getCountries(), vendor.country::in)
                .buildAnd();

        Page<Vendor> vendors = vendorRepository.findAll(predicate, pageRequest);

        return vendors.getContent().stream()
                .map(VendorMapper.INSTANCE::vendorToVendorResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public VendorResponseDto findVendorById(Long vendorId) {
        return VendorMapper.INSTANCE
                .vendorToVendorResponseDto(repository.findById(vendorId)
                        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    @Transactional
    @Override
    public void deleteVendor(Long vendorId) {
        getVendor(vendorId);
        repository.deleteById(vendorId);
    }

    @Transactional
    @Override
    public VendorResponseDto addVendorImage(Long vendorId, MultipartFile file) {
        Vendor vendor = getVendor(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        vendor.setImage(ImageMapper.INSTANCE.imageDtoToImage(imageDto));
        return VendorMapper.INSTANCE.vendorToVendorResponseDto(vendor);
    }

    @Transactional
    @Override
    public void deleteVendorImage(Long vendorId) {
        Vendor vendor = getVendor(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
    }

    private Vendor getVendor(Long vendorId) {
        return repository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}