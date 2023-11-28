package ru.softplat.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.exception.DuplicateException;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.model.admin.Admin;
import ru.softplat.repository.admin.AdminRepository;

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
    public Admin getAdmin(String email) {
        return adminRepository
                .findByEmail(email).orElseThrow(
                        () -> new EntityNotFoundException(
                                ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(email, Admin.class)
                        ));
    }

    @Transactional(readOnly = true)
    public boolean checkIfUserExistsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }
}
