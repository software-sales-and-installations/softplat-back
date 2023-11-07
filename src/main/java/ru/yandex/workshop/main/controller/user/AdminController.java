package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.admin.AdminService;
import ru.yandex.workshop.security.dto.UserDto;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public void addAdmin(@RequestBody @Valid UserDto userDto) { //TODO New.class!!!!
        log.info(LogMessage.TRY_ADD_ADMIN.label);
        adminService.addAdmin(userDto);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public AdminResponseDto getAdminByEmail(Principal principal) {
        log.info(LogMessage.TRY_GET_ADMIN.label, principal.getName());
        AdminResponseDto response = adminService.getAdminDto(principal.getName());
        log.info("{}", response);
        return response;
    }
}
