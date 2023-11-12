package ru.yandex.workshop.main.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.user.SellerDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.main.service.seller.SellerService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
@Slf4j
public class SellerController {
    private final SellerService sellerService;
    private final SellerBankService bankService;

    @GetMapping
    @Operation(summary = "Получение списка продавцов", description = "Доступ для всех")
    public List<SellerResponseDto> getAllSellers(@RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_All_SELLERS.label);
        return sellerService.getAllSellers(from, size);
    }

    @Operation(summary = "Получение конкретного продавца по userId", description = "Доступ для всех")
    @GetMapping("/{userId}")
    public SellerResponseDto getSeller(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_GET_SELLER.label, userId);
        return sellerService.getSellerDto(userId);
    }

    @Operation(summary = "Обновление данных о себе продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping
    public SellerResponseDto updateSeller(Principal principal, @RequestBody @Valid SellerDto sellerForUpdate) {
        log.debug(LogMessage.TRY_PATCH_SELLER.label, principal.getName());
        return sellerService.updateSeller(principal.getName(), sellerForUpdate);
    }

    @Operation(summary = "Получение своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping("/bank")
    public BankRequisitesDto getRequisites(Principal principal) {
        log.debug(LogMessage.TRY_SELLER_GET_REQUISITES.label, principal.getName());
        return bankService.getRequisites(principal.getName());
    }

    @Operation(summary = "Обновление своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping("/bank")
    public BankRequisitesDto updateRequisites(Principal principal, @RequestBody BankRequisitesDto requisites) {
        log.debug(LogMessage.TRY_SELLER_PATCH_REQUISITES.label, principal.getName());
        return bankService.updateRequisites(principal.getName(), requisites);
    }

    @Operation(summary = "Удаление своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping("/bank")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(Principal principal) {
        log.debug(LogMessage.TRY_SELLER_DELETE_REQUISITES.label, principal.getName());
        bankService.deleteRequisites(principal.getName());
    }

    @Operation(summary = "Добавление/обновление изображения своего профиля продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping("/account/image")
    public SellerResponseDto addSellerImage(Principal principal, @RequestParam(value = "image") MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        return sellerService.addSellerImage(principal.getName(), image);
    }

    @Operation(summary = "Удаление изображения своего профиля продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/account/image")
    public void deleteOwnImage(Principal principal) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);
        sellerService.deleteSellerImage(principal.getName());
    }

    @Operation(summary = "Удаление изображения профиля продавца админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sellerId}/image")
    public void deleteSellerImage(@PathVariable Long sellerId) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);
        sellerService.deleteSellerImageBySellerId(sellerId);
    }
}
