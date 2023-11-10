package ru.yandex.workshop.main.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.user.mapper.AdminMapper;
import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.main.repository.admin.AdminRepository;
import ru.yandex.workshop.security.dto.UserDto;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    @Transactional
    public void addAdmin(UserDto userDto) {
        if (checkIfUserExistsByEmail(userDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);

        adminRepository.save(AdminMapper.INSTANCE.userDtoToAdmin(userDto));
    }

    public AdminResponseDto getAdminDto(String email) {
        return AdminMapper.INSTANCE.adminToAdminResponseDto(getAdmin(email));
    }


    private Admin getAdmin(String email) {
        return adminRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private boolean checkIfUserExistsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }
}
