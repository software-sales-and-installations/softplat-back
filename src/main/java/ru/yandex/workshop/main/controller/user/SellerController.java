package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.dto.seller.SellerForResponse;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.main.service.seller.SellerService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
public class SellerController {

    private final SellerService sellerService;
    private final SellerBankService bankService;

    @GetMapping("/{email}")
    public SellerForResponse getSeller(@PathVariable String email) {
        return sellerService.getSeller(email);
    }

    @PostMapping("/registration")
    public SellerForResponse addSeller(@RequestBody @Valid SellerDto sellerDto) {
        return sellerService.addSeller(sellerDto);
    }

    @PatchMapping("/account/{email}")
    public SellerForResponse updateSeller(@PathVariable String email, @RequestBody @Valid SellerForUpdate sellerForUpdate) {
        return sellerService.updateSeller(email, sellerForUpdate);
    }

    @GetMapping("/account/bank/{email}")
    public BankRequisitesDto getRequisites(@PathVariable String email) {
        return bankService.getRequisites(email);
    }

    @PatchMapping("/account/bank/{email}")
    public BankRequisitesDto updateRequisites(@PathVariable String email, @RequestBody BankRequisitesDto requisites) {
        return bankService.updateRequisites(email, requisites);
    }

    @DeleteMapping("/account/bank/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(@PathVariable String email) {
        bankService.deleteRequisites(email);
    }
}
