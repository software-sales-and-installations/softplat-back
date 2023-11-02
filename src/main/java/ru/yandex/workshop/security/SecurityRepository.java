package ru.yandex.workshop.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.security.model.UserAuth;

import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByEmail(String email);
}
