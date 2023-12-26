package ru.softplat.main.server.web.controller.vendor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.main.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.main.dto.vendor.VendorResponseDto;
import ru.softplat.main.dto.vendor.VendorSearchRequestDto;
import ru.softplat.main.dto.vendor.VendorsListResponseDto;
import ru.softplat.main.server.mapper.ImageMapper;
import ru.softplat.main.server.mapper.VendorMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.image.Image;
import ru.softplat.main.server.model.vendor.Vendor;
import ru.softplat.main.server.service.vendor.VendorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/vendor")
public class VendorController {
    private final VendorService service;
    private final VendorMapper vendorMapper;
    private final ImageMapper imageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorResponseDto createVendor(@RequestBody VendorCreateUpdateDto vendorCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        Vendor vendor = service.createVendor(vendorCreateUpdateDto);
        return vendorMapper.vendorToVendorResponseDto(vendor);
    }

    @PatchMapping(path = "/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody VendorCreateUpdateDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);
        Vendor response = service.changeVendorById(vendorId, vendorUpdateDto);
        return vendorMapper.vendorToVendorResponseDto(response);
    }

    @GetMapping("/search")
    public VendorsListResponseDto findVendorWithFilers(
            @RequestBody(required = false) VendorSearchRequestDto vendorSearchRequestDto,
            @RequestParam int minId, @RequestParam int pageSize) {
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
                                               @RequestBody ImageCreateDto imageCreateDto) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        Image image = imageMapper.toImage(imageCreateDto);
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
