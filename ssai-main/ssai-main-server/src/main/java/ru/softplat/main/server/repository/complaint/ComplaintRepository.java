package ru.softplat.main.server.repository.complaint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.complaint.Complaint;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllBySellerId(Long sellerId, Pageable pageable);

    boolean existsByBuyerIdAndProductId(long buyerId, long productId);

    List<Complaint> findAllByProductId(long productId, Pageable pageable);

    boolean existsByIdAndBuyerId(long id, long buyerId);
}
