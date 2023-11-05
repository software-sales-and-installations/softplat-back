package ru.yandex.workshop.main.controller.vendor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorUpdateDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.vendor.VendorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/vendor")
public class VendorController {
    private final VendorService service;

    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VendorResponseDto createVendor(@RequestBody @Valid VendorDto vendorDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.createVendor(vendorDto);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                              @RequestBody @Valid VendorUpdateDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);
        return service.changeVendorById(vendorId, vendorUpdateDto);
    }

    @GetMapping
    public List<VendorResponseDto> findVendorAll() {
        log.debug(LogMessage.TRY_GET_VENDOR.label);
        return service.findVendorAll();
    }

    @GetMapping(path = "/{vendorId}")
    public VendorResponseDto findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_GET_ID_VENDOR.label);
        return service.findVendorById(vendorId);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{vendorId}")
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_VENDOR.label);
        service.deleteVendor(vendorId);
    }

}
