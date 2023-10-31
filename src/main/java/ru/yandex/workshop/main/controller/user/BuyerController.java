package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.buyer.BuyerDto;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.service.buyer.BuyerService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping
    public BuyerResponseDto addNewBuyer(@RequestBody @Validated(New.class) BuyerDto buyerDto) {
        log.info("POST request for new buyer: {}", buyerDto);
        BuyerResponseDto response = buyerService.addNewBuyer(buyerDto);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/{buyerId}")
    public BuyerResponseDto getBuyerById(@PathVariable(name = "buyerId") long buyerId) {
        log.info("GET request for buyer with id: {}", buyerId);
        BuyerResponseDto response = buyerService.getBuyer(buyerId);
        log.info("{}", response);
        return response;
    }

    @PatchMapping("/{buyerId}")
    public BuyerResponseDto updateBuyerById(@PathVariable(name = "buyerId") long buyerId,
            @RequestBody @Valid BuyerDto buyerDto) {
        log.info("PATCH request for buyer with id {}: {}", buyerId, buyerDto);
        BuyerResponseDto response = buyerService.updateBuyer(buyerId, buyerDto);
        log.info("{}", response);
        return response;
    }

}
