package ru.softplat.main.server.service.complaint;

import ru.softplat.main.server.model.complaint.Complaint;

import java.util.List;

public interface ComplaintService {

    List<Complaint> getAllComplaints();

    List<Complaint> getAllSellerComplaints(Long sellerId);

    Complaint createComplaint(Long userId, Long productId, String reason);
}
