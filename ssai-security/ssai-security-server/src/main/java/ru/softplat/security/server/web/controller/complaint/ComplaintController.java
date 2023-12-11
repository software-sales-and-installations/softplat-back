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
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Slf4j
public class ComplaintController {
    private final ComplaintClient complaintClient;

    @Operation(summary = "Получение всех жалоб админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public ResponseEntity<Object> getComplaintListForAdmin() {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label);
        return complaintClient.getComplaintListForAdmin();
    }

    @Operation(summary = "Создание жалобы покупателем", description = "Доступ покупатель")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/buyer/{userId}/{productId}/complaint")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComplaint(@PathVariable Long userId,
                                                  @PathVariable Long productId,
                                                  @RequestParam ComplaintReason reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label);
        return complaintClient.createComplaint(userId, productId, reason);
    }

    @Operation(summary = "Получение всех жалоб продавцом", description = "Доступ продавец")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping("/seller/{userId}/complaints")
    public ResponseEntity<Object> getComplaintListForSeller(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label);
        return complaintClient.getComplaintListForSeller(userId);
    }
}
