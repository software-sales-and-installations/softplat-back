package ru.softplat.main.server.web.controller.vendor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.main.dto.vendor.VendorResponseDto;
import ru.softplat.main.dto.vendor.VendorSearchRequestDto;
import ru.softplat.main.dto.vendor.VendorsListResponseDto;
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

    @PostMapping
    public VendorResponseDto createVendor(@RequestBody @Validated(New.class) VendorCreateUpdateDto vendorCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        Vendor vendor = service.createVendor(vendorCreateUpdateDto);
        return vendorMapper.vendorToVendorResponseDto(vendor);
    }

    @PatchMapping(path = "/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody @Valid VendorCreateUpdateDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);
        Vendor response = service.changeVendorById(vendorId, vendorUpdateDto);
        return vendorMapper.vendorToVendorResponseDto(response);
    }

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

    @GetMapping(path = "/{vendorId}")
    public VendorResponseDto findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_GET_ID_VENDOR.label);
        Vendor response = service.getVendorById(vendorId);
        return vendorMapper.vendorToVendorResponseDto(response);
    }

    @DeleteMapping(path = "/{vendorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_VENDOR.label);
        service.deleteVendor(vendorId);
    }

    @PostMapping(path = "/{vendorId}/image")
    @ResponseStatus(value = HttpStatus.CREATED)
    public VendorResponseDto createVendorImage(@PathVariable(name = "vendorId") Long vendorId,
                                               @RequestParam(value = "image") @MultipartFileFormat MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        Vendor response = service.addVendorImage(vendorId, image);
        return vendorMapper.vendorToVendorResponseDto(response);
    }

    @DeleteMapping(path = "/{vendorId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteVendorImage(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);
        service.deleteVendorImage(vendorId);
    }
}
