package ru.softplat.security.server.web.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.client.user.SellerClient;
import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.user.SellerUpdateDto;
import ru.softplat.security.server.message.LogMessage;
import ru.softplat.security.server.web.validation.MultipartFileFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
@Slf4j
public class SellerController {
    private final SellerClient sellerClient;

    @Operation(summary = "Получение списка продавцов", description = "Доступ для всех")
    @GetMapping
    public ResponseEntity<Object> getAllSellers(@RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
                                                @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_All_SELLERS.label);
        return sellerClient.getAllSellers(minId, pageSize);
    }

    @Operation(summary = "Получение конкретного продавца по userId", description = "Доступ для всех")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getSeller(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_GET_SELLER.label, userId);
        return sellerClient.getSeller(userId);
    }

    @Operation(summary = "Обновление данных о себе продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping
    public ResponseEntity<Object> updateSeller(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid SellerUpdateDto sellerUpdateDto) {
        log.debug(LogMessage.TRY_PATCH_SELLER.label, userId);
        return sellerClient.updateSeller(userId, sellerUpdateDto);
    }

    @Operation(summary = "Получение банковских реквизитов по id продавца", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping("/bank")
    public ResponseEntity<Object> getRequisitesSeller(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(LogMessage.TRY_SELLER_GET_REQUISITES.label, userId);
        return sellerClient.getRequisitesSeller(userId);
    }

    @Operation(summary = "Получение банковских реквизитов по id продавца", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping("/bank/{userId}")
    public ResponseEntity<Object> getRequisitesAdmin(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_SELLER_GET_REQUISITES.label, userId);
        return sellerClient.getRequisitesAdmin(userId);
    }

    @Operation(summary = "Обновление своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping("/bank")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> updateRequisites(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestBody @Valid BankRequisitesCreateUpdateDto requisites) {
        log.debug(LogMessage.TRY_SELLER_PATCH_REQUISITES.label, userId);
        return sellerClient.updateRequisites(userId, requisites);
    }

    @Operation(summary = "Удаление своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping("/bank")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug(LogMessage.TRY_SELLER_DELETE_REQUISITES.label, userId);
        sellerClient.deleteRequisites(userId);
    }

    @Operation(summary = "Добавление/обновление изображения своего профиля продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping("/account/image")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> addSellerImage(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(value = "image") @MultipartFileFormat MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        return sellerClient.addSellerImage(userId, image);
    }

    @Operation(summary = "Удаление изображения своего профиля продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping("/account/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnImage(@RequestHeader("X-Sharer-User-Id") long userId) {
        sellerClient.deleteOwnImage(userId);
    }

    @Operation(summary = "Удаление изображения профиля продавца админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping("/{sellerId}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSellerImage(@PathVariable Long sellerId) {
        sellerClient.deleteSellerImage(sellerId);
    }
}
