package ru.yandex.workshop.main.web.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.yandex.workshop.main.dto.seller.BankRequisitesResponseDto;
import ru.yandex.workshop.main.dto.user.SellerUpdateDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.mapper.SellerMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.service.seller.SellerBankService;
import ru.yandex.workshop.main.service.seller.SellerService;
import ru.yandex.workshop.main.web.validation.MultipartFileFormat;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
@Slf4j
public class SellerController {
    private final SellerService sellerService;
    private final SellerBankService bankService;
    private final SellerMapper sellerMapper;

    @Operation(summary = "Получение списка продавцов", description = "Доступ для всех")
    @GetMapping
    public List<SellerResponseDto> getAllSellers(@RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
                                                 @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_All_SELLERS.label);
        List<Seller> response = sellerService.getAllSellers(minId, pageSize);
        return response.stream()
                .map(sellerMapper::sellerToSellerResponseDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получение конкретного продавца по userId", description = "Доступ для всех")
    @GetMapping("/{userId}")
    public SellerResponseDto getSeller(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_GET_SELLER.label, userId);
        Seller response = sellerService.getSeller(userId);
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @Operation(summary = "Обновление данных о себе продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping
    public SellerResponseDto updateSeller(@ApiIgnore Principal principal, @RequestBody @Valid SellerUpdateDto sellerUpdateDto) {
        log.debug(LogMessage.TRY_PATCH_SELLER.label, principal.getName());
        Seller updateRequest = sellerMapper.sellerDtoToSeller(sellerUpdateDto);
        Seller response = sellerService.updateSeller(principal.getName(), updateRequest);
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @Operation(summary = "Получение банковских реквизитов продавцом по id продавца", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping("/bank/{userId}")
    public BankRequisitesResponseDto getRequisites(@PathVariable Long userId) {
        log.debug(LogMessage.TRY_SELLER_GET_REQUISITES.label, userId);
        BankRequisites response = bankService.getRequisites(userId);
        return sellerMapper.requisitesToDto(response);
    }

    @Operation(summary = "Обновление своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PatchMapping("/bank")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BankRequisitesResponseDto updateRequisites(@ApiIgnore Principal principal,
                                                      @RequestBody @Valid BankRequisitesCreateUpdateDto requisites) {
        log.debug(LogMessage.TRY_SELLER_PATCH_REQUISITES.label, principal.getName());
        BankRequisites response = bankService.updateRequisites(principal.getName(), new BankRequisites(null,
                requisites.getAccount()));
        return sellerMapper.requisitesToDto(response);
    }

    @Operation(summary = "Удаление своих банковских реквизитов продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping("/bank")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(@ApiIgnore Principal principal) {
        log.debug(LogMessage.TRY_SELLER_DELETE_REQUISITES.label, principal.getName());
        bankService.deleteRequisites(principal.getName());
    }

    @Operation(summary = "Добавление/обновление изображения своего профиля продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @PostMapping("/account/image")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SellerResponseDto addSellerImage(@ApiIgnore Principal principal,
                                            @RequestParam(value = "image") @MultipartFileFormat MultipartFile image) {
        log.debug(LogMessage.TRY_ADD_IMAGE.label);
        Seller response = sellerService.addSellerImage(principal.getName(), image);
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @Operation(summary = "Удаление изображения своего профиля продавцом", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @DeleteMapping("/account/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnImage(@ApiIgnore Principal principal) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);
        sellerService.deleteSellerImage(principal.getName());
    }

    @Operation(summary = "Удаление изображения профиля продавца админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping("/{sellerId}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSellerImage(@PathVariable Long sellerId) {
        log.debug(LogMessage.TRY_DElETE_IMAGE.label);
        sellerService.deleteSellerImageBySellerId(sellerId);
    }
}
