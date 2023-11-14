package ru.yandex.workshop.main.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.main.repository.admin.AdminRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private AdminRepository adminRepository;

    private Admin admin;

    @BeforeEach
    void init() {
        admin = Admin.builder().email("email@email.com").build();
    }

    @Test
    void addAdmin_whenAdminAlreadyExists_throwDuplicateException() {
        when(adminRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(admin));

        assertThrows(DuplicateException.class,
                () -> adminService.addAdmin(admin));
    }

    @Test
    void addAdmin_whenValid_thenReturnNewAdmin() {
        when(adminRepository.save(any())).thenReturn(admin);

        Admin expect = admin;
        Admin actual = adminRepository.save(admin);
        assertEquals(expect, actual);
    }

    @Test
    void getAdminDto_whenAlreadyExists_throwEntityNotFoundException() {
        when(adminRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> adminService.getAdmin(admin.getEmail()));
    }

    @Test
    void getAdminDto_whenValid_thenReturnAdminEntityFromRepository() {
        when(adminRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(admin));

        Admin expect = admin;
        Admin actual = adminService.getAdmin(admin.getEmail());
        assertEquals(expect, actual);
    }
}