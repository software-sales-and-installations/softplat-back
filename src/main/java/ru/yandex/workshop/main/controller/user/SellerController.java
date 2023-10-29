package ru.yandex.workshop.main.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.seller.BankRequisitesDto;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.dto.seller.SellerForResponse;
import ru.yandex.workshop.main.dto.seller.SellerForUpdate;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.main.service.seller.SellerService;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping(path = "/seller")
public class SellerController {
    private final SellerService sellerService;
    private final SellerBankService bankService;

    @Autowired
    public SellerController(SellerService sellerService, SellerBankService bankService) {
        this.sellerService = sellerService;
        this.bankService = bankService;
    }

    @GetMapping("/account")
    public SellerForResponse getSeller(String email) {
        return sellerService.getSeller(email);
    }

    @PostMapping("/registration")
    public SellerForResponse addSeller(@Valid SellerDto sellerDto) {
        return sellerService.addSeller(sellerDto);
    }

    @PatchMapping("/account")
    public SellerForResponse updateSeller(String email, @Valid SellerForUpdate sellerForUpdate) {
        return sellerService.updateSeller(email, sellerForUpdate);
    }

    @GetMapping("/account/bank")
    public BankRequisitesDto getRequisites(String email) {
        return bankService.getRequisites(email);
    }

    @PatchMapping("/account/bank")
    public BankRequisitesDto updateRequisites(String email, @Pattern(regexp = "[0-9]{16}",
            message = "Номер счета должен содержать 16 цифр 0-9") String requisites) {
        return bankService.updateRequisites(email, requisites);
    }

    @DeleteMapping("/account/bank")
    public void deleteRequisites(String email) {
        bankService.deleteRequisites(email);
    }
}
