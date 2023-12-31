package ru.softplat.security.server.web.controller.vendor;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.client.vendor.VendorClient;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.Update;
import ru.softplat.main.dto.vendor.VendorCreateUpdateDto;
import ru.softplat.main.dto.vendor.VendorResponseDto;
import ru.softplat.main.dto.vendor.VendorSearchRequestDto;
import ru.softplat.main.dto.vendor.VendorsListResponseDto;
import ru.softplat.security.server.mapper.MultipartFileMapper;
import ru.softplat.security.server.message.LogMessage;
import ru.softplat.security.server.web.validation.MultipartFileFormat;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/vendor")
public class VendorController {
    private final VendorClient vendorClient;
    private final MultipartFileMapper multipartFileMapper;

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = VendorResponseDto.class)})
    @Operation(summary = "Добавление вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createVendor(@RequestBody @Validated(New.class) VendorCreateUpdateDto vendorCreateUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_ADD_VENDOR.label);

        return vendorClient.createVendor(vendorCreateUpdateDto);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = VendorResponseDto.class)})
    @Operation(summary = "Изменение информации о вендоре", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/{vendorId}", produces = "application/json")
    public ResponseEntity<Object> changeVendorById(@PathVariable(name = "vendorId") Long vendorId,
                                                   @RequestBody @Validated(Update.class) VendorCreateUpdateDto vendorUpdateDto) {
        log.debug(LogMessage.TRY_ADMIN_PATCH_VENDOR.label);

        return vendorClient.changeVendorById(vendorId, vendorUpdateDto);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = VendorsListResponseDto.class)})
    @Operation(summary = "Получение списка вендоров с фильтрацией", description = "Доступ для всех")
    @GetMapping(path = "/search", produces = "application/json")
    public ResponseEntity<Object> findVendorWithFilers(
            @RequestBody(required = false) VendorSearchRequestDto vendorSearchRequestDto,
            @RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
            @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_VENDORS.label);

        return vendorClient.findVendorWithFilers(vendorSearchRequestDto, minId, pageSize);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = VendorResponseDto.class)})
    @Operation(summary = "Получение вендора по id", description = "Доступ для всех")
    @GetMapping(path = "/{vendorId}", produces = "application/json")
    public ResponseEntity<Object> findVendorById(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_GET_ID_VENDOR.label);

        return vendorClient.findVendorById(vendorId);
    }

    @Operation(summary = "Удаление вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{vendorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendor(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_ADMIN_DELETE_VENDOR.label);

        vendorClient.deleteVendor(vendorId);
    }

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = VendorResponseDto.class)})
    @Operation(summary = "Добавление/обновление изображения вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @PostMapping(path = "/{vendorId}/image", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createVendorImage(@PathVariable(name = "vendorId") Long vendorId,
                                                    @RequestBody @MultipartFileFormat MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        ImageCreateDto imageCreateDto = multipartFileMapper.toImageDto(image);

        return vendorClient.createVendorImage(vendorId, imageCreateDto);
    }

    @Operation(summary = "Удаление изображения вендора", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{vendorId}/image")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteVendorImage(@PathVariable(name = "vendorId") Long vendorId) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);

        vendorClient.deleteVendorImage(vendorId);
    }
}
