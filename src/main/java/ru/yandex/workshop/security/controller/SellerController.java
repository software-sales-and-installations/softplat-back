package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.security.dto.response.SellerResponseDto;
import ru.yandex.workshop.security.service.SellerDetailsServiceImpl;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
@Slf4j
public class SellerController {

    private final SellerDetailsServiceImpl sellerService;
    private final SellerBankService bankService;

    @GetMapping("/{email}")
    public SellerResponseDto getSeller(@PathVariable String email) {
        log.debug(LogMessage.TRY_GET_SELLER.label, email);
        return sellerService.getSeller(email);
    }

    @PatchMapping("/account/{email}")
    public SellerResponseDto updateSeller(@PathVariable String email, @RequestBody @Valid SellerForUpdate sellerForUpdate) {
        log.debug(LogMessage.TRY_PATCH_SELLER.label, email);
        return sellerService.updateSeller(email, sellerForUpdate);
    }

    @GetMapping("/account/bank/{email}")
    public BankRequisitesDto getRequisites(@PathVariable String email) {
        log.debug(LogMessage.TRY_SELLER_GET_REQUISITES.label, email);
        return bankService.getRequisites(email);
    }

    @PatchMapping("/account/bank/{email}")
    public BankRequisitesDto updateRequisites(@PathVariable String email, @RequestBody BankRequisitesDto requisites) {
        log.debug(LogMessage.TRY_SELLER_PATCH_REQUISITES.label, email);
        return bankService.updateRequisites(email, requisites);
    }

    @DeleteMapping("/account/bank/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(@PathVariable String email) {
        log.debug(LogMessage.TRY_SELLER_DELETE_REQUISITES.label, email);
        bankService.deleteRequisites(email);
    }
}
