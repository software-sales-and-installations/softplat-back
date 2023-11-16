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
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorFilter;
import ru.yandex.workshop.main.dto.vendor.VendorUpdateDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.mapper.ImageMapper;
import ru.yandex.workshop.main.mapper.VendorMapper;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.vendor.QVendor;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.main.util.QPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final VendorRepository repository;
    private final ImageService imageService;
    private final VendorMapper vendorMapper;
    private final ImageMapper imageMapper;

    @Transactional
    @Override
    public Vendor createVendor(VendorDto vendorDto) {
        return repository.save(vendorMapper.vendorDtoToVendor(vendorDto));
    }

    @Transactional
    @Override
    public Vendor changeVendorById(Long vendorId, VendorUpdateDto vendorUpdateDto) {
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

        return repository.save(oldVendor);
    }

    @Override
    public List<Vendor> findVendorsWithFilter(VendorFilter vendorFilter, int from, int size) {
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

        Page<Vendor> vendors = repository.findAll(predicate, pageRequest);

        return new ArrayList<>(vendors.getContent());
    }

    @Override
    public Vendor getVendorById(Long vendorId) {
        return repository.findById(vendorId).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
        );
    }

    @Transactional
    @Override
    public void deleteVendor(Long vendorId) {
        getVendor(vendorId);
        repository.deleteById(vendorId);
    }

    @Transactional
    @Override
    public Vendor addVendorImage(Long vendorId, MultipartFile file) {
        Vendor vendor = getVendor(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
        ImageDto imageDto = imageService.addNewImage(file);
        vendor.setImage(imageMapper.imageDtoToImage(imageDto));
        return vendor;
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