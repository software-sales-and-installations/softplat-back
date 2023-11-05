package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.security.dto.response.AdminResponseDto;
import ru.yandex.workshop.security.service.AdminDetailsServiceImpl;

import java.security.Principal;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminDetailsServiceImpl adminService;

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public AdminResponseDto getAdminByEmail(Principal principal) {
        log.info(LogMessage.TRY_GET_ADMIN.label, principal.getName());
        AdminResponseDto response = adminService.getAdminDto(principal.getName());
        log.info("{}", response);
        return response;
    }
}
