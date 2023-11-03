package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.dto.seller.SellerResponseDto;
import ru.yandex.workshop.main.dto.seller.SellerUpdateDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.main.service.seller.SellerService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
@Slf4j
public class SellerController {

    private final SellerService sellerService;
    private final SellerBankService bankService;

    @GetMapping("/{email}")
    public SellerResponseDto getSeller(@PathVariable String email) {
        log.debug(LogMessage.TRY_GET_SELLER.label, email);
        return sellerService.getSeller(email);
    }

    @PostMapping("/registration")
    public SellerResponseDto addSeller(@RequestBody @Valid SellerDto sellerDto) {
        log.debug(LogMessage.TRY_ADD_SELLER.label);
        return sellerService.addSeller(sellerDto);
    }

    @PatchMapping("/account/{email}")
    public SellerResponseDto updateSeller(@PathVariable String email,
                                          @RequestBody @Valid SellerUpdateDto sellerUpdateDto) {
        log.debug(LogMessage.TRY_PATCH_SELLER.label, email);
        return sellerService.updateSeller(email, sellerUpdateDto);
    }

    @PostMapping("/account/{email}/image")
    public SellerResponseDto addSellerImage(@PathVariable String email,
                                            @RequestParam(value = "image") MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_SELLER_IMAGE.label);
        return sellerService.addSellerImage(email, image);
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
