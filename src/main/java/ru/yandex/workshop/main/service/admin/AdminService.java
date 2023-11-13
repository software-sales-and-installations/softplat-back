package ru.yandex.workshop.main.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.mapper.AdminMapper;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.main.repository.admin.AdminRepository;
import ru.yandex.workshop.security.dto.UserDto;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public void addAdmin(UserDto userDto) {
        if (checkIfUserExistsByEmail(userDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);

        adminRepository.save(adminMapper.userDtoToAdmin(userDto));
    }

    @Transactional(readOnly = true)
    public Admin getAdmin(String email) {
        return adminRepository
                .findByEmail(email).orElseThrow(
                        () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
                );
    }

    @Transactional(readOnly = true)
    public boolean checkIfUserExistsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }
}
