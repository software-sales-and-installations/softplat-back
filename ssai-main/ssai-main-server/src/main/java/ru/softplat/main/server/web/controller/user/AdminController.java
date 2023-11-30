package ru.softplat.main.server.web.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.user.UserCreateMainDto;
import ru.softplat.main.dto.user.response.AdminResponseDto;
import ru.softplat.main.server.mapper.AdminMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.admin.Admin;
import ru.softplat.main.server.service.admin.AdminService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping
    public AdminResponseDto getAdminById(Long adminId) {
        log.info(LogMessage.TRY_GET_ADMIN.label, adminId);
        Admin response = adminService.getAdmin(adminId);
        return adminMapper.adminToAdminResponseDto(response);
    }

    @GetMapping
    public AdminResponseDto addAdmin(UserCreateMainDto userCreateMainDto) {
//        log.info(LogMessage.TRY_GET_ADMIN.label, adminId);

        Admin response = adminService.addAdmin(adminMapper.adminFromUserCreateMainDto(userCreateMainDto));
        return adminMapper.adminToAdminResponseDto(response);
    }
}
