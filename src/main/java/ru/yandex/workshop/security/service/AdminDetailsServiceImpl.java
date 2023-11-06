package ru.yandex.workshop.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.security.dto.UserDetailsImpl;
import ru.yandex.workshop.security.dto.registration.RegistrationAdminDto;
import ru.yandex.workshop.security.dto.response.AdminResponseDto;
import ru.yandex.workshop.security.mapper.AdminMapper;
import ru.yandex.workshop.security.model.user.Admin;
import ru.yandex.workshop.security.repository.AdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = getAdmin(email);
        return UserDetailsImpl.fromUser(admin);
    }

    @Transactional
    public AdminResponseDto addAdmin(RegistrationAdminDto registrationAdminDto) {
        if (checkIfUserExistsByEmail(registrationAdminDto.getEmail()))
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);

        Admin admin = AdminMapper.INSTANCE.adminDtoToAdmin(registrationAdminDto);
        admin.setPassword(passwordEncoder.encode(registrationAdminDto.getPassword()));

        return AdminMapper.INSTANCE.adminToAdminResponseDto(adminRepository.save(admin));
    }

    public List<AdminResponseDto> getAllAdmins() { //TODO delete?
        return adminRepository.findAll(Pageable.ofSize(10)).stream()//TODO common custom pagination
                .map(AdminMapper.INSTANCE::adminToAdminResponseDto)
                .collect(Collectors.toList());
    }

    public AdminResponseDto getAdminDto(String email) {
        return AdminMapper.INSTANCE.adminToAdminResponseDto(getAdmin(email));
    }

    private Admin getAdmin(String email) {
        return adminRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    public boolean checkIfUserExistsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }
}
