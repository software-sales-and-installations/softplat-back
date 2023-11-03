package ru.yandex.workshop.security.service.user;

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
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.dto.response.AdminResponseDto;
import ru.yandex.workshop.security.mapper.AdminMapper;
import ru.yandex.workshop.security.model.Admin;
import ru.yandex.workshop.security.repository.AdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = getAdminFromDatabase(email);
        return AdminResponseDto.fromUser(admin);
    }

    @Transactional
    public AdminResponseDto addAdmin(RegistrationUserDto registrationUserDto) {
        getAdminFromDatabase(registrationUserDto.getEmail());
        Admin admin = Admin.builder()
                .email(registrationUserDto.getEmail())
                .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                .name(registrationUserDto.getName())
                .role(registrationUserDto.getRole())
                .build();
        try {
            return AdminMapper.INSTANCE.adminToAdminResponseDto(adminRepository.save(admin));
        } catch (Exception e) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }

//    TODO
//    @Transactional
//    public SellerResponseDto updateAdmin(String email, Admin AdminDto) {
//    }

    public List<AdminResponseDto> getAllSellers() {
        return adminRepository.findAll(Pageable.ofSize(10)).stream()//TODO common custom pagination
                .map(AdminMapper.INSTANCE::adminToAdminResponseDto)
                .collect(Collectors.toList());
    }

    public AdminResponseDto getAdmin(String email) {
        return AdminMapper.INSTANCE.adminToAdminResponseDto(adminRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
    }

    private Admin getAdminFromDatabase(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

}
