package ru.softplat.main.server.web.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.dto.user.SellerUpdateDto;
import ru.softplat.main.dto.user.response.SellerResponseDto;
import ru.softplat.main.dto.user.response.SellersListResponseDto;
import ru.softplat.main.server.mapper.BankRequisitesMapper;
import ru.softplat.main.server.mapper.SellerMapper;
import ru.softplat.main.server.model.seller.BankRequisites;
import ru.softplat.main.server.model.seller.Seller;
import ru.softplat.main.server.service.seller.SellerBankService;
import ru.softplat.main.server.service.seller.SellerService;
import ru.softplat.security.dto.UserCreateMainDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/seller")
public class SellerController {
    private final SellerService sellerService;
    private final SellerBankService bankService;
    private final SellerMapper sellerMapper;
    private final BankRequisitesMapper requisitesMapper;

    @PostMapping
    public SellerResponseDto addSeller(@RequestBody UserCreateMainDto userCreateMainDto) {
        Seller response = sellerService.addSeller(sellerMapper.userCreateDtoToSeller(userCreateMainDto));
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @GetMapping
    public SellersListResponseDto getAllSellers(int minId, int pageSize) {
        List<Seller> sellerList = sellerService.getAllSellers(minId, pageSize);
        List<SellerResponseDto> response = sellerList.stream()
                .map(sellerMapper::sellerToSellerResponseDto)
                .collect(Collectors.toList());
        return sellerMapper.toSellersListResponseDto(response);
    }

    @GetMapping("/{userId}")
    public SellerResponseDto getSeller(@PathVariable Long userId) {
        Seller response = sellerService.getSeller(userId);
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @PatchMapping
    public SellerResponseDto updateSeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestBody SellerUpdateDto sellerUpdateDto) {
        Seller updateRequest = sellerMapper.sellerDtoToSeller(sellerUpdateDto);
        Seller response = sellerService.updateSeller(userId, updateRequest);
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeller(@PathVariable long userId) {
        sellerService.deleteSeller(userId);
    }

    @GetMapping("/bank")
    public BankRequisitesResponseDto getRequisitesSeller(@RequestHeader("X-Sharer-User-Id") Long userId) {
        BankRequisites response = bankService.getRequisites(userId);
        return requisitesMapper.requisitesToDto(response);
    }

    @GetMapping("/bank/{userId}")
    public BankRequisitesResponseDto getRequisitesAdmin(@RequestHeader("X-Sharer-User-Id") Long userId) {
        BankRequisites response = bankService.getRequisites(userId);
        return requisitesMapper.requisitesToDto(response);
    }

    @PostMapping("/bank")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BankRequisitesResponseDto addRequisites(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestBody BankRequisitesCreateUpdateDto requisites) {
        BankRequisites response = bankService.addRequisites(userId,
                requisitesMapper.createUpdateDtoToRequisites(requisites));
        return requisitesMapper.requisitesToDto(response);
    }

    @PatchMapping("/bank")
    public BankRequisitesResponseDto updateRequisites(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestBody BankRequisitesCreateUpdateDto requisites) {
        BankRequisites response = bankService.updateRequisites(userId,
                requisitesMapper.createUpdateDtoToRequisites(requisites));
        return requisitesMapper.requisitesToDto(response);
    }

    @DeleteMapping("/bank")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequisites(@RequestHeader("X-Sharer-User-Id") long userId) {
        bankService.deleteRequisites(userId);
    }

    @PostMapping("/account/image")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SellerResponseDto addSellerImage(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(value = "image") MultipartFile image) {
        Seller response = sellerService.addSellerImage(userId, image);
        return sellerMapper.sellerToSellerResponseDto(response);
    }

    @DeleteMapping("/account/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnImage(@RequestHeader("X-Sharer-User-Id") long userId) {
        sellerService.deleteSellerImage(userId);
    }

    @DeleteMapping("/{sellerId}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSellerImage(@PathVariable Long sellerId) {
        sellerService.deleteSellerImage(sellerId);
    }
}
