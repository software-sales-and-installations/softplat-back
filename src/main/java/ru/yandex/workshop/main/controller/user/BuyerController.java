package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.dto.validation.ValidBuyer;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.buyer.BuyerServiceImpl;
import ru.yandex.workshop.security.dto.UserDto;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerServiceImpl buyerService;

    @PostMapping
    public void addBuyer(@Validated(ValidBuyer.class) UserDto userDto) {
        log.info(LogMessage.TRY_ADD_BUYER.label);
        buyerService.addBuyer(userDto);
    }

    @GetMapping("/{userId}")
    public BuyerResponseDto getBuyer(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_BUYER.label, userId);
        BuyerResponseDto response = buyerService.getBuyer(userId);
        log.info("{}", response);
        return response;
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping
    public BuyerResponseDto updateBuyer(Principal principal, @RequestBody @Valid BuyerDto buyerDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, principal.getName());
        BuyerResponseDto response = buyerService.updateBuyer(principal.getName(), buyerDto);
        log.info("{}", response);
        return response;
    }

}
