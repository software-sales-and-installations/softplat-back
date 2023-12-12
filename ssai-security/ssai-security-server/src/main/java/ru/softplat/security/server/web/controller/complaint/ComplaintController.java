package ru.softplat.security.server.web.controller.complaint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.complaint.ComplaintClient;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintStatus;
import ru.softplat.security.server.message.LogMessage;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Slf4j
public class ComplaintController {

    // TODO перенести сюда логирование из мейна
    // TODO добавить описание модели данных для сваггера

    private final ComplaintClient complaintClient;

    @Operation(summary = "Создание жалобы покупателем", description = "Доступ покупатель")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComplaint(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long productId,
                                                  @RequestParam @NotNull ComplaintReason reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label);
        return complaintClient.createComplaint(userId, productId, reason);
    }

    @Operation(summary = "Получение всех жалоб админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public ResponseEntity<Object> getComplaintListForAdmin(@RequestParam(defaultValue = "0") int minId,
                                                           @RequestParam(defaultValue = "20") int pageSize) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label);
        return complaintClient.getComplaintListForAdmin(minId, pageSize);
    }

    @Operation(summary = "Получение жалоб по продукту админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/{productId}/product", produces = "application/json")
    public ResponseEntity<Object> getComplaintsForProductByAdmin(@PathVariable long productId,
                                                                 @RequestParam(defaultValue = "0") int minId,
                                                                 @RequestParam(defaultValue = "20") int pageSize) {
        return complaintClient.getComplaintsForProductByAdmin(productId, minId, pageSize);
    }

    @Operation(summary = "Получение жалоб по id админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/{complaintId}", produces = "application/json")
    public ResponseEntity<Object> getComplaintByIdByAdmin(@PathVariable long complaintId) {
//        log.info(LogMessage.TRY_GET_COMPLAINT.label, complaintId);
        return complaintClient.getComplaintByIdByAdmin(complaintId);
    }

    @Operation(summary = "Отправка жалобы на модерацию админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @PatchMapping("/admin/{complaintId}")
    public ResponseEntity<Object> sendProductOnModerationByAdmin(@PathVariable long complaintId,
                                                               @RequestParam ComplaintStatus status,
                                                               @RequestBody String comment) {
//        log.info(LogMessage.TRY_SEND_PRODUCT_ON_MODERATION_BY_ADMIN.label);
        return complaintClient.sendProductOnModerationByAdmin(complaintId, status, comment);
    }

    @Operation(summary = "Получение всех жалоб продавцом", description = "Доступ продавец")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping("/seller/{userId}/complaints")
    public ResponseEntity<Object> getComplaintListForSeller(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label);
        return complaintClient.getComplaintListForSeller(userId);
    }
}
