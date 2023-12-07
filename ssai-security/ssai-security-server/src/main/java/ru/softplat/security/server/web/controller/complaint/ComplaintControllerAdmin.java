package ru.softplat.security.server.web.controller.complaint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.client.complaint.ComplaintAdminClient;
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("complaint/admin")
@Slf4j
public class ComplaintControllerAdmin {
    private final ComplaintAdminClient complaintAdminClient;

    @Operation(summary = "Получение всех жалоб админом", description = "Доступ админ")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping("/complaints")
    public ResponseEntity<Object> getComplaintListForAdmin(@RequestHeader("X-Sharer-User-Id") Long adminId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label, adminId);
        return complaintAdminClient.getComplaintListForAdmin(adminId);
    }
}
