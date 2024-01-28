package ru.softplat.security.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.security.server.model.ResetToken;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ResetToken, Long> {

	ResetToken findByConfirmationToken(String confirmationToken);

	void deleteByConfirmationToken(String confirmationToken);
}