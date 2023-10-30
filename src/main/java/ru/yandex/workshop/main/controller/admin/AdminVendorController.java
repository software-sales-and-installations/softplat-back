package ru.yandex.workshop.main.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.vendor.VendorDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.admin.vendor.AdminVendorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminVendorController {
    @Autowired
    private AdminVendorService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/vendor")
    public VendorResponseDto createVendor(@RequestBody @Valid VendorDto vendorDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.createVendor(vendorDto);
    }

    @PatchMapping(path = "/vendor/{vendorId}")
    public VendorResponseDto changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                           @RequestBody VendorDto vendorDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.changeVendorById(vendorId, vendorDto);
    }

    @GetMapping(path = "/vendor")
    public List<VendorResponseDto> findVendor(@RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "country", required = false) String country) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.findVendor(name, country);
    }

    @GetMapping(path = "/vendor/{vendorId}")
    public VendorResponseDto findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        return service.findVendorById(vendorId);
    }

    @DeleteMapping(path = "/vendor/{vendorId}")
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);
        service.deleteVendor(vendorId);
    }

}
