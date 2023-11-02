package ru.yandex.workshop.main.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.main.dto.vendor.VendorUpdateDto;
import ru.yandex.workshop.main.main.service.vendor.VendorService;
import ru.yandex.workshop.main.main.message.LogMessage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VendorController {
    private final VendorService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/admin/vendor")
    public VendorResponseDto createVendor(@RequestBody @Valid VendorDto vendorDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.createVendor(vendorDto);
    }

    @PatchMapping(path = "/admin/vendor/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody @Valid VendorUpdateDto vendorUpdateDto) {
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
