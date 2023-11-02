package ru.yandex.workshop.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.security.dto.RegistrationUserDto;
import ru.yandex.workshop.security.model.UserAuth;

import java.util.Optional;

@Slf4j
@Service
public class UserAuthService implements UserDetailsService {
    private SecurityRepository securityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserAuth> findByEmail(String email) {
        return securityRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuth userAuth = findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Пользователь с email '%s' не найден", email)));
        return new User(userAuth.getEmail(), userAuth.getPassword(),
                userAuth.getRole().getAuthorities());
    }

    public void createNewUser(RegistrationUserDto registrationUserDto) {
        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(registrationUserDto.getEmail());
        userAuth.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        userAuth.setRole(registrationUserDto.getRole());
        securityRepository.save(userAuth);
    }
}
