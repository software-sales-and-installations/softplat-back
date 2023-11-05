package ru.yandex.workshop.main.controller.vendor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.vendor.VendorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VendorController {
    private final VendorService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/admin/vendor")
    public VendorResponseDto createVendor(@RequestBody @Validated(New.class) VendorDto vendorDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.createVendor(vendorDto);
    }

    @PostMapping(path = "admin/vendor/{vendorId}/image")
    public VendorResponseDto createVendorImage(@PathVariable(name = "vendorId") Long vendorId,
                                               @RequestParam(value = "image") MultipartFile image) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR_IMAGE.label);
        return service.addVendorImage(vendorId, image);
    }

    @DeleteMapping(path = "admin/vendor/{vendorId}/image")
    public void deleteVendorImage(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DElETE_VENDOR_IMAGE.label);
        service.deleteVendorImage(vendorId);
    }

    @PatchMapping(path = "/admin/vendor/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody @Valid VendorDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);
        return service.changeVendorById(vendorId, vendorUpdateDto);
    }

    @GetMapping(path = "/vendor")
    public List<VendorResponseDto> findVendorAll() {
        log.debug(LogMessage.TRY_GET_VENDOR.label);
        return service.findVendorAll();
    }

    @GetMapping(path = "/vendor/{vendorId}")
    public VendorResponseDto findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_GET_ID_VENDOR.label);
        return service.findVendorById(vendorId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/admin/vendor/{vendorId}")
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_VENDOR.label);
        service.deleteVendor(vendorId);
    }

}
