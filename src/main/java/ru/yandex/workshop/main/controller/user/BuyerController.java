package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.buyer.BuyerService;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerService buyerService;

    @PreAuthorize("hasAuthority('buyer:write') and hasAuthority('seller:write')")
    @GetMapping("/{userId}")
    public BuyerResponseDto getBuyer(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_BUYER.label, userId);
        return buyerService.getBuyer(userId);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping
    public BuyerResponseDto updateBuyer(Principal principal, @RequestBody @Valid BuyerDto buyerDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, principal.getName());
        return buyerService.updateBuyer(principal.getName(), buyerDto);
    }

}
