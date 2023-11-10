package ru.yandex.workshop.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.workshop.security.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
