package ru.softplat.security.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.security.server.exception.EntityNotFoundException;
import ru.softplat.security.server.message.ExceptionMessage;
import ru.softplat.security.server.model.UserSecurity;
import ru.softplat.security.server.repository.UserRepository;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return UserSecurity.fromUser(repository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(email))));
    }
}
