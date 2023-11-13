//package ru.yandex.workshop.main.service.admin;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.yandex.workshop.main.mapper.AdminMapper;
//import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
//import ru.yandex.workshop.main.exception.DuplicateException;
//import ru.yandex.workshop.main.exception.EntityNotFoundException;
//import ru.yandex.workshop.main.model.admin.Admin;
//import ru.yandex.workshop.main.repository.admin.AdminRepository;
//import ru.yandex.workshop.security.dto.UserDto;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class AdminServiceTest {
//
//    @InjectMocks
//    private AdminService adminService;
//    @Mock
//    private AdminRepository adminRepository;
//
//    private UserDto userDto;
//    private Admin admin;
//
//    @BeforeEach
//    void init() {
//        userDto = UserDto.builder().email("email@email.com").build();
//        admin = AdminMapper.INSTANCE.userDtoToAdmin(userDto);
//    }
//
//    @Test
//    void addAdmin_whenAdminAlreadyExists_throwDuplicateException() {
//        when(adminRepository.findByEmail(anyString()))
//                .thenReturn(Optional.of(admin));
//
//        assertThrows(DuplicateException.class,
//                () -> adminService.addAdmin(userDto));
//    }
//
//    @Test
//    void addAdmin_whenValid_thenReturnNewAdmin() {
//        when(adminRepository.save(any())).thenReturn(admin);
//
//        Admin expect = admin;
//        Admin actual = adminRepository.save(admin);
//        assertEquals(expect, actual);
//    }
//
//    @Test
//    void getAdminDto_whenAlreadyExists_throwEntityNotFoundException() {
//        when(adminRepository.findByEmail(anyString()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class,
//                () -> adminService.getAdminDto(userDto.getEmail()));
//    }
//
//    @Test
//    void getAdminDto_whenValid_thenReturnAdminEntityFromRepository() {
//        when(adminRepository.findByEmail(anyString()))
//                .thenReturn(Optional.of(admin));
//
//        AdminResponseDto expect = AdminMapper.INSTANCE.adminToAdminResponseDto(admin);
//        AdminResponseDto actual = adminService.getAdminDto(userDto.getEmail());
//        assertEquals(expect, actual);
//    }
//}