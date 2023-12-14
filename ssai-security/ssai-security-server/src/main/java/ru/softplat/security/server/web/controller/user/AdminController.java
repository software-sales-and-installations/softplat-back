package ru.softplat.security.server.web.controller.user;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.client.user.AdminClient;
import ru.softplat.main.dto.user.response.AdminResponseDto;
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminClient adminClient;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = AdminResponseDto.class)})
    @Operation(summary = "Получение информации об админе - просмотр своего ЛК", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAdminById(@RequestHeader("X-Sharer-User-Id") Long adminId) {
        log.info(LogMessage.TRY_GET_ADMIN.label, adminId);
        return adminClient.getAdminById(adminId);
    }
}
