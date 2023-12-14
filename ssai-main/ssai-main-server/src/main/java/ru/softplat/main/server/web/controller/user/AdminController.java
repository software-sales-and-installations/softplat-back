package ru.softplat.main.server.web.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.user.response.AdminResponseDto;
import ru.softplat.main.server.mapper.AdminMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.admin.Admin;
import ru.softplat.main.server.service.admin.AdminService;
import ru.softplat.security.dto.UserCreateMainDto;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping
    public AdminResponseDto getAdminById(@RequestHeader("X-Sharer-User-Id") Long adminId) {
        log.info(LogMessage.TRY_GET_ADMIN.label, adminId);
        Admin response = adminService.getAdmin(adminId);
        return adminMapper.adminToAdminResponseDto(response);
    }

    @PostMapping
    public AdminResponseDto addAdmin(@RequestBody UserCreateMainDto userCreateMainDto) {
        Admin response = adminService.addAdmin(adminMapper.adminFromUserCreateMainDto(userCreateMainDto));
        return adminMapper.adminToAdminResponseDto(response);
    }
}
