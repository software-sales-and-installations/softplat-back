package ru.yandex.workshop.main.controller.vendor;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorFilter;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.vendor.VendorService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/vendor")
public class VendorController {
    private final VendorService service;

    @Operation(summary = "Добавление вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VendorResponseDto createVendor(@RequestBody @Validated(New.class) VendorDto vendorDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.createVendor(vendorDto);
    }

    @Operation(summary = "Изменение информации о вендоре", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody @Valid VendorDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);
        return service.changeVendorById(vendorId, vendorUpdateDto);
    }

    @Operation(summary = "Получение списка вендоров с фильтрацией", description = "Доступ для всех")
    @GetMapping(path = "/vendor")
    public List<VendorResponseDto> findVendorWithFilers(
            @RequestBody VendorFilter vendorFilter,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_VENDORS.label);
        return service.findVendorAll(vendorFilter, from, size);
    }

    @Operation(summary = "Получение вендора по id", description = "Доступ для всех")
    @GetMapping(path = "/{vendorId}")
    public VendorResponseDto findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_GET_ID_VENDOR.label);
        return service.findVendorById(vendorId);
    }

    @Operation(summary = "Удаление вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{vendorId}")
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_VENDOR.label);
        service.deleteVendor(vendorId);
    }

    @Operation(summary = "Добавление/обновление изображения вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(path = "/{vendorId}/image")
    public VendorResponseDto createVendorImage(@PathVariable(name = "vendorId") Long vendorId,
                                               @RequestParam(value = "image") MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        return service.addVendorImage(vendorId, image);
    }

    @Operation(summary = "Удаление изображения вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{vendorId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteVendorImage(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);
        service.deleteVendorImage(vendorId);
    }
}
