package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.service.SellerDetailsServiceImpl;

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

    private final SellerDetailsServiceImpl sellerService;
    private final SellerBankService bankService;

    @GetMapping
    public List<SellerResponseDto> getAllSellers(@RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        log.debug(LogMessage.TRY_GET_All_SELLERS.label);
        return sellerService.getAllSellers(from, size);
    }

    @GetMapping("/{userId}")
    public SellerResponseDto getSeller(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_GET_SELLER.label, userId);
        return sellerService.getSeller(userId);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping
    public SellerResponseDto updateSeller(Principal principal, @RequestBody @Valid SellerForUpdate sellerForUpdate) {
        log.debug(LogMessage.TRY_PATCH_SELLER.label, principal.getName());
        return sellerService.updateSeller(principal.getName(), sellerForUpdate);
    }

    @GetMapping("/bank/{userId}")
    public BankRequisitesDto getRequisites(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_SELLER_GET_REQUISITES.label, userId);
        return bankService.getRequisites(userId);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping("/bank")
    public BankRequisitesDto updateRequisites(Principal principal, @RequestBody BankRequisitesDto requisites) {
        log.debug(LogMessage.TRY_SELLER_PATCH_REQUISITES.label, principal.getName());
        return bankService.updateRequisites(principal.getName(), requisites);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping("/bank")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(Principal principal) {
        log.debug(LogMessage.TRY_SELLER_DELETE_REQUISITES.label, principal.getName());
        bankService.deleteRequisites(principal.getName());
    }
}
