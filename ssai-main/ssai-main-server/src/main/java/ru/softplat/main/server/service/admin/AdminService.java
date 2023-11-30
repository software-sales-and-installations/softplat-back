package ru.softplat.main.server.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.repository.admin.AdminRepository;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.admin.Admin;

import javax.persistence.EntityNotFoundException;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin addAdmin(Admin admin) {
        if (checkIfUserExistsByEmail(admin.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);

        return adminRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public Admin getAdmin(Long adminId) {
        return adminRepository
                .findById(adminId).orElseThrow(
                        () -> new EntityNotFoundException(
                                ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(adminId.toString(), Admin.class)
                        ));
    }

    @Transactional(readOnly = true)
    public boolean checkIfUserExistsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }
}
