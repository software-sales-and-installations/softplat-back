package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.security.dto.response.BuyerResponseDto;
import ru.yandex.workshop.security.dto.user.BuyerDto;
import ru.yandex.workshop.security.service.user.BuyerService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @GetMapping("/{emailBuyer}")
    public BuyerResponseDto getBuyerById(@PathVariable(name = "emailBuyer") String emailBuyer) {
        log.info(LogMessage.TRY_GET_BUYER.label, emailBuyer);
        BuyerResponseDto response = buyerService.getBuyer(emailBuyer);
        log.info("{}", response);
        return response;
    }

    @PatchMapping("/{emailBuyer}")
    public BuyerResponseDto updateBuyerById(@PathVariable(name = "emailBuyer") String emailBuyer,
                                            @RequestBody @Valid BuyerDto buyerDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, emailBuyer);
        BuyerResponseDto response = buyerService.updateBuyer(emailBuyer, buyerDto);
        log.info("{}", response);
        return response;
    }

}
