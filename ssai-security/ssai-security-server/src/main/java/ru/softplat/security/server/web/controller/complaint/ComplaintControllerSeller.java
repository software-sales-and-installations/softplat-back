package ru.softplat.security.server.web.controller.complaint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.client.complaint.ComplaintSellerClient;
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("complaint/seller")
@Slf4j
public class ComplaintControllerSeller {
    private final ComplaintSellerClient complaintSellerClient;

    @Operation(summary = "Получение всех жалоб продавцом", description = "Доступ продавец")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping("/{userId}/complaints")
    public ResponseEntity<Object> getComplaintListForSeller(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label);
        return complaintSellerClient.getComplaintListForSeller(userId);
    }
}
