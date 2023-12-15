package ru.softplat.security.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softplat.security.server.model.Role;
import ru.softplat.security.server.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdMainAndRole(long userId, Role role);

}
