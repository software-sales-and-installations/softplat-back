package ru.softplat.main.server.service.vendor;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.main.dto.vendor.VendorSearchRequestDto;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.VendorMapper;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.vendor.QVendor;
import ru.softplat.main.server.model.vendor.Vendor;
import ru.softplat.main.server.repository.vendor.VendorRepository;
import ru.softplat.main.server.service.image.ImageService;
import ru.softplat.main.server.util.QPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final VendorRepository repository;
    private final ImageService imageService;
    private final VendorMapper vendorMapper;

    @Override
    public Vendor createVendor(VendorCreateUpdateDto vendorCreateUpdateDto) {
        checkIfExistsByName(vendorCreateUpdateDto.getName());
        return repository.save(vendorMapper.vendorDtoToVendor(vendorCreateUpdateDto));
    }

    @Override
    public Vendor changeVendorById(Long vendorId, VendorCreateUpdateDto vendorUpdateDto) {
        Vendor oldVendor = getVendorById(vendorId);

        if (vendorUpdateDto.getName() != null) {
            if (vendorUpdateDto.getName().isBlank())
                throw new WrongConditionException("Введите корректное название.");
            checkIfExistsByName(vendorUpdateDto.getName());
            oldVendor.setName(vendorUpdateDto.getName());
        }
        if (vendorUpdateDto.getDescription() != null) {
            if (vendorUpdateDto.getDescription().isBlank())
                throw new WrongConditionException("Введите корректное описание.");
            oldVendor.setDescription(vendorUpdateDto.getDescription());
        }
        if (vendorUpdateDto.getCountry() != null) {
            oldVendor.setCountry(vendorUpdateDto.getCountry());
        }

        return repository.save(oldVendor);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Vendor getVendorById(Long vendorId) {
        return repository.findById(vendorId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(vendorId, Vendor.class)
                ));
    }

    @Override
    public void deleteVendor(Long vendorId) {
        getVendorById(vendorId);
        repository.deleteById(vendorId);
    }

    @Override
    public Vendor addVendorImage(Long vendorId, MultipartFile file) {
        Vendor vendor = getVendorById(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
        vendor.setImage(imageService.addNewImage(file));
        return vendor;
    }

    @Override
    public void deleteVendorImage(Long vendorId) {
        Vendor vendor = getVendorById(vendorId);
        if (vendor.getImage() != null) {
            imageService.deleteImageById(vendor.getImage().getId());
        }
    }

    private void checkIfExistsByName(String name) {
        if (repository.existsByName(name))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
    }
}