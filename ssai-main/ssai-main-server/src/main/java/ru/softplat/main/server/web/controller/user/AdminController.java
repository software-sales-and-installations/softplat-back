package ru.softplat.main.server.web.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.dto.user.response.AdminResponseDto;
import ru.softplat.main.server.mapper.AdminMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.admin.Admin;
import ru.softplat.main.server.service.admin.AdminService;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Operation(summary = "Получение информации об админе - просмотр своего ЛК", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public AdminResponseDto getAdminByEmail(@ApiIgnore Principal principal) {
        log.info(LogMessage.TRY_GET_ADMIN.label, principal.getName());
        Admin response = adminService.getAdmin(principal.getName());
        return adminMapper.adminToAdminResponseDto(response);
    }
}
