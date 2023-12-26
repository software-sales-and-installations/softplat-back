package ru.softplat.security.server.web.controller.complaint;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.complaint.ComplaintClient;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.dto.compliant.ComplaintUpdateDto;
import ru.softplat.security.server.message.LogMessage;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Slf4j
public class ComplaintController {
    private final ComplaintClient complaintClient;

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = ComplaintResponseDto.class)})
    @Operation(summary = "Создание жалобы покупателем", description = "Доступ покупатель")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping(path = "/{productId}", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComplaint(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long productId,
                                                  @RequestParam @NotNull ComplaintReason reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label);
        return complaintClient.createComplaint(userId, productId, reason);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintListResponseDto.class)})
    @Operation(summary = "Получение всех жалоб админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin", produces = "application/json")
    public ResponseEntity<Object> getComplaintListForAdmin(@RequestParam(defaultValue = "0") @Min(0) int minId,
                                                           @RequestParam(defaultValue = "20") @Min(1) int pageSize) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label);
        return complaintClient.getComplaintListForAdmin(minId, pageSize);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintListResponseDto.class)})
    @Operation(summary = "Получение жалоб по продукту админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/{productId}/product", produces = "application/json")
    public ResponseEntity<Object> getComplaintsForProductByAdmin(@PathVariable long productId,
                                                                 @RequestParam(defaultValue = "0") @Min(0) int minId,
                                                                 @RequestParam(defaultValue = "20") @Min(1) int pageSize) {
        log.info(LogMessage.TRY_GET_PRODUCT_COMPLAINTS.label);
        return complaintClient.getComplaintsForProductByAdmin(productId, minId, pageSize);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintResponseDto.class)})
    @Operation(summary = "Получение жалоб по id админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/{complaintId}", produces = "application/json")
    public ResponseEntity<Object> getComplaintByIdByAdmin(@PathVariable long complaintId) {
        log.info(LogMessage.TRY_GET_COMPLAINT.label, complaintId);
        return complaintClient.getComplaintByIdByAdmin(complaintId);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintResponseDto.class)})
    @Operation(summary = "Отправка жалобы на модерацию админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping(path = "/admin/{complaintId}", produces = "application/json")
    public ResponseEntity<Object> sendProductOnModerationByAdmin(@PathVariable long complaintId,
                                                                 @RequestBody @NotNull ComplaintUpdateDto updateDto) {
        log.info(LogMessage.TRY_SEND_PRODUCT_ON_MODERATION_BY_ADMIN.label);
        return complaintClient.sendProductOnModerationByAdmin(complaintId, updateDto);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintListResponseDto.class)})
    @Operation(summary = "Получение всех жалоб продавцом", description = "Доступ продавец")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller", produces = "application/json")
    public ResponseEntity<Object> getComplaintListForSeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @RequestParam(defaultValue = "0") @Min(0) int minId,
                                                            @RequestParam(defaultValue = "20") @Min(1) int pageSize) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label);
        return complaintClient.getComplaintListForSeller(userId, minId, pageSize);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintListResponseDto.class)})
    @Operation(summary = "Получение жалоб по продукту продавцом", description = "Доступ продавец")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller/{productId}/product", produces = "application/json")
    public ResponseEntity<Object> getComplaintsForProductBySeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                  @PathVariable long productId,
                                                                  @RequestParam(defaultValue = "0") @Min(0) int minId,
                                                                  @RequestParam(defaultValue = "20") @Min(1) int pageSize) {
        log.info(LogMessage.TRY_GET_PRODUCT_COMPLAINTS.label);
        return complaintClient.getComplaintsForProductBySeller(userId, productId, minId, pageSize);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ComplaintResponseDto.class)})
    @Operation(summary = "Получение жалобы по id продавцом", description = "Доступ продавец")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller/{complaintId}", produces = "application/json")
    public ResponseEntity<Object> getComplaintById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PathVariable long complaintId) {
        log.info(LogMessage.TRY_GET_COMPLAINT.label, complaintId);
        return complaintClient.getComplaintById(userId, complaintId);
    }
}
