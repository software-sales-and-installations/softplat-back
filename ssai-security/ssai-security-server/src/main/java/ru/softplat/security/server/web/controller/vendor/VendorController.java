package ru.softplat.security.server.web.controller.vendor;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.dto.validation.New;
import ru.softplat.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.dto.vendor.VendorResponseDto;
import ru.softplat.dto.vendor.VendorSearchRequestDto;
import ru.softplat.dto.vendor.VendorsListResponseDto;
import ru.softplat.main.server.mapper.VendorMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.vendor.Vendor;
import ru.softplat.main.server.service.vendor.VendorService;
import ru.softplat.main.server.web.validation.MultipartFileFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/vendor")
public class VendorController {
    private final VendorService service;
    private final VendorMapper vendorMapper;

    @Operation(summary = "Добавление вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorResponseDto createVendor(@RequestBody @Validated(New.class) VendorCreateUpdateDto vendorCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        Vendor vendor = service.createVendor(vendorCreateUpdateDto);
        return vendorMapper.vendorToVendorResponseDto(vendor);
    }

    @Operation(summary = "Изменение информации о вендоре", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody @Valid VendorCreateUpdateDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);
        Vendor response = service.changeVendorById(vendorId, vendorUpdateDto);
        return vendorMapper.vendorToVendorResponseDto(response);
    }

    @Operation(summary = "Получение списка вендоров с фильтрацией", description = "Доступ для всех")
    @GetMapping("/search")
    public VendorsListResponseDto findVendorWithFilers(
            @RequestBody(required = false) VendorSearchRequestDto vendorSearchRequestDto,
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_VENDORS.label);
        List<Vendor> vendorList = service.findVendorsWithFilter(vendorSearchRequestDto, minId, pageSize);
        List<VendorResponseDto> response = vendorList.stream()
                .map(vendorMapper::vendorToVendorResponseDto)
                .collect(Collectors.toList());
        return vendorMapper.toVendorsListResponseDto(response);
    }

    @Operation(summary = "Получение вендора по id", description = "Доступ для всех")
    @GetMapping(path = "/{vendorId}")
    public VendorResponseDto findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_GET_ID_VENDOR.label);
        Vendor response = service.getVendorById(vendorId);
        return vendorMapper.vendorToVendorResponseDto(response);
    }

    @Operation(summary = "Удаление вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{vendorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_VENDOR.label);
        service.deleteVendor(vendorId);
    }

    @Operation(summary = "Добавление/обновление изображения вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(path = "/{vendorId}/image")
    @ResponseStatus(value = HttpStatus.CREATED)
    public VendorResponseDto createVendorImage(@PathVariable(name = "vendorId") Long vendorId,
                                               @RequestParam(value = "image") @MultipartFileFormat MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        Vendor response = service.addVendorImage(vendorId, image);
        return vendorMapper.vendorToVendorResponseDto(response);
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