package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.security.dto.response.AdminResponseDto;
import ru.yandex.workshop.security.service.user.AdminService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{emailAdmin}")
    public AdminResponseDto getAdminByEmail(@PathVariable(name = "emailAdmin") String email) {
        log.info(LogMessage.TRY_GET_BUYER.label, email);
        AdminResponseDto response = adminService.getAdmin(email);
        log.info("{}", response);
        return response;
    }

//    TODO
//    @PatchMapping("/{emailAdmin}")
//    public AdminResponseDto updateAdminByEmail(@PathVariable(name = "emailAdmin") String emailAdmin,
//    }
}
