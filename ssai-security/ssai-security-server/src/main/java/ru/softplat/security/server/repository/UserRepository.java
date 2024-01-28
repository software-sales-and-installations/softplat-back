package ru.softplat.security.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.softplat.security.server.model.Role;
import ru.softplat.security.server.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdMainAndRole(long userId, Role role);

}
