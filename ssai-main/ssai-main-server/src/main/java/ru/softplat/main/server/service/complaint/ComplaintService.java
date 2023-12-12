package ru.softplat.main.server.service.complaint;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintStatus;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.AccessDenialException;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.repository.complaint.ComplaintRepository;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.buyer.OrderService;
import ru.softplat.main.server.service.product.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final ProductService productService;
    private final BuyerService buyerService;
    private final OrderService orderService;

    public Complaint createComplaint(Long userId, Long productId, ComplaintReason reason) {
        orderService.checkBuyerAccessRightsToCreateComment(userId, productId);
        checkIfComplaintAlreadyExists(userId, productId);

        Complaint newComplaint = initComplaint(userId, productId, reason);
        productService.updateProductComplaintCountOnCreate(productId);
        return complaintRepository.save(newComplaint);
    }

    @Transactional(readOnly = true)
    public List<Complaint> getAllComplaints(int from, int size) {
        Sort sortBy = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);
        return complaintRepository.findAll(pageRequest).getContent();
    }

    @Transactional(readOnly = true)
    public long countAllComplaintsForAdmin() {
        return complaintRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Complaint> getAllSellerComplaints(long sellerId, int from, int size) {
        Sort sortBy = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);
        return complaintRepository.findAllByProductSellerId(sellerId, pageRequest);
    }

    @Transactional(readOnly = true)
    public long countAllComplaintsBySellerId(long sellerId) {
        return complaintRepository.countAllByProductSellerId(sellerId);
    }

    @Transactional(readOnly = true)
    public List<Complaint> getAllProductComplaints(long productId, int from, int size) {
        Sort sortBy = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);
        return complaintRepository.findAllByProductId(productId, pageRequest);
    }

    @Transactional(readOnly = true)
    public long countAllByProductId(long productId) {
        return complaintRepository.countAllByProductId(productId);
    }

    @Transactional(readOnly = true)
    public Complaint getComplaintById(long complaintId) {
        String message = ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(complaintId, Complaint.class);
        Optional<Complaint> response = complaintRepository.findById(complaintId);
        return response.orElseThrow(() -> new EntityNotFoundException(message));
    }

    private Complaint initComplaint(long userId, long productId, ComplaintReason reason) {
        Buyer buyer = buyerService.getBuyer(userId);
        Product product = productService.getAvailableProduct(productId);

        return Complaint.builder()
                .createdAt(LocalDateTime.now())
                .reason(reason)
                .complaintStatus(ComplaintStatus.OPENED)
                .product(product)
                .buyer(buyer)
                .build();
    }

    public Complaint updateComplaintByAdmin(long complaintId, String adminComment, ComplaintStatus status) {
        Complaint complaint = getComplaintById(complaintId);
        long productId = complaint.getProduct().getId();

        if (status.equals(ComplaintStatus.REVIEW)) {
            if (adminComment != null) {
                complaint.setAdminComment(adminComment);
            }
            productService.updateStatus(productId, ProductStatus.DRAFT);
        } else if (status.equals(ComplaintStatus.CLOSED)) {
            productService.updateProductComplaintCountOnDelete(productId);
        } else {
            throw new WrongConditionException(ExceptionMessage.WRONG_CONDITION_EXCEPTION.label);
        }

        complaint.setComplaintStatus(status);
        return complaintRepository.save(complaint);
    }

    public void updateProductComplaintsBySeller(long productId) {
        List<Complaint> productComplaints = complaintRepository.findByProductId(productId);
        for (Complaint complaint : productComplaints) {
            complaint.setComplaintStatus(ComplaintStatus.REVIEW);
            complaintRepository.save(complaint);
        }
    }

    @Transactional(readOnly = true)
    public void checkSellerRightToViewComplaints(long userId, long productId) {
        productService.checkSellerAccessRights(userId, productId);
    }

    @Transactional(readOnly = true)
    public void checkSellerRightToViewComplaint(long complaintId, long sellerId) {
        if (!complaintRepository.existsByIdAndProductSellerId(complaintId, sellerId)) {
            throw new AccessDenialException(ExceptionMessage.COMPLAINT_NO_RIGHTS_EXCEPTION.label);
        }
    }

    private void checkIfComplaintAlreadyExists(long userId, long productId) {
        if (complaintRepository.existsByBuyerIdAndProductId(userId, productId)) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }
}
