package ru.yandex.workshop.main.service.vendor;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorSearchRequestDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
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

    @Transactional
    @Override
    public Vendor createVendor(VendorCreateUpdateDto vendorCreateUpdateDto) {
        return repository.save(vendorMapper.vendorDtoToVendor(vendorCreateUpdateDto));
    }

    @Transactional
    @Override
    public Vendor changeVendorById(Long vendorId, VendorCreateUpdateDto vendorUpdateDto) {
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
    public List<Vendor> findVendorsWithFilter(VendorSearchRequestDto vendorSearchRequestDto, int from, int size) {
        PageRequest pageRequest = PageRequestOverride.of(from, size);

        QVendor vendor = QVendor.vendor;

        Function<String, Predicate> textPredicateFunction = text -> {
            BooleanExpression nameExpression = vendor.name.toLowerCase().like("%" + text.toLowerCase() + "%");
            BooleanExpression descriptionExpression = vendor.description.toLowerCase().like("%" + text.toLowerCase() + "%");
            return nameExpression.or(descriptionExpression);
        };

        Predicate predicate;
        if (vendorSearchRequestDto != null) {
            predicate = QPredicates.builder()
                    .add(vendorSearchRequestDto.getText(), textPredicateFunction)
                    .add(vendorSearchRequestDto.getCountries(), vendor.country::in)
                    .buildAnd();
        } else {
            predicate = QPredicates.builder().buildAnd();
        }

        if (predicate != null) {
            return new ArrayList<>(repository.findAll(predicate, pageRequest).getContent());
        }
        return new ArrayList<>(repository.findAll(pageRequest).getContent());
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
        vendor.setImage(imageService.addNewImage(file));
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