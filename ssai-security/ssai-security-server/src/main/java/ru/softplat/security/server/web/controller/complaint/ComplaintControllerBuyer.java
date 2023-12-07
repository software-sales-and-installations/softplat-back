package ru.softplat.security.server.web.controller.complaint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.complaint.ComplaintBuyerClient;
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("complaint/buyer")
@Slf4j
public class ComplaintControllerBuyer {
    private final ComplaintBuyerClient complaintBuyerClient;

    @Operation(summary = "Получение списка вариантов жалоб", description = "Доступ покупатель")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/complaints")
    public ResponseEntity<Object> getAllComplaintReasons() {
        log.info(LogMessage.TRY_GET_LIST_COMPLAINTS_BUYER.label);
        return complaintBuyerClient.getAllComplaintReasons();
    }

    @Operation(summary = "Создание жалобы покупателем", description = "Доступ покупатель")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/{userId}/{productId}/complaint")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComplaint(@PathVariable Long userId,
                                                  @PathVariable Long productId,
                                                  @RequestBody String reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label);
        return complaintBuyerClient.createComplaint(userId, productId, reason);
    }
}
