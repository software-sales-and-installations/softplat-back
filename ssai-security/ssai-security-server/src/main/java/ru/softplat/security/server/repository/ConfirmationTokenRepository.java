package ru.softplat.security.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.security.server.model.ResetToken;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ResetToken, Long> {

	ResetToken findByConfirmationToken(String confirmationToken);

	void deleteByConfirmationToken(String confirmationToken);
}