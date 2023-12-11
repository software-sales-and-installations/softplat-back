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
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.repository.complaint.ComplaintRepository;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.buyer.OrderService;
import ru.softplat.main.server.service.product.ProductService;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final ProductService productService;
    private final BuyerService buyerService;
    private final OrderService orderService;

    public Complaint createComplaint(Long userId, Long productId, ComplaintReason reason) {
        checkIfComplaintAlreadyExists(userId, productId);

        Buyer buyer = buyerService.getBuyer(userId);
        Product product = productService.getAvailableProduct(productId);
        Order order = orderService.getOrderByBuyerIdAndProductId(userId, productId);

        Complaint newComplaint = initComplaint(buyer, product, order, reason);
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
    public List<Complaint> getAllSellerComplaints(long sellerId, int from, int size) {
        Sort sortBy = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);
        return complaintRepository.findAllBySellerId(sellerId, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Complaint> getAllProductComplaints(long productId, int from, int size) {
        Sort sortBy = Sort.by("createdAt").descending();
        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);
        return complaintRepository.findAllByProductId(productId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Complaint getComplaintById(long complaintId) {
        String message = ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(complaintId, Complaint.class);
        Optional<Complaint> response = complaintRepository.findById(complaintId);
        return response.orElseThrow(() -> new EntityNotFoundException(message));
    }

    private Complaint initComplaint(Buyer buyer, Product product, Order order, ComplaintReason reason) {
        return Complaint.builder()
                .seller(product.getSeller())
                .createdAt(now())
                .reason(reason)
                .product(product)
                .order(order)
                .buyer(buyer)
                .build();
    }

    public Complaint sendProductOnModerationByAdmin(long complaintId, String adminComment, ComplaintStatus status) {
        Complaint complaint = getComplaintById(complaintId);
        long productId = complaint.getProduct().getId();

        if (status.equals(ComplaintStatus.REVIEWED_BY_ADMIN)) {
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

    public Complaint updateProductDueToComplaint(long complaintId, long productId, Product productForUpdate) {
        Complaint complaint = getComplaintById(complaintId);
        productService.checkSellerAccessRights(complaint.getSeller().getId(), productId);
        productService.update(productId, productForUpdate);
        complaint.setComplaintStatus(ComplaintStatus.REVIEWED_BY_SELLER);
        return complaint;
    }

    @Transactional(readOnly = true)
    public void checkSellerRightToViewComplaints(long userId, long productId) {
        productService.checkSellerAccessRights(userId, productId);
    }

    @Transactional(readOnly = true)
    public void checkSellerRightToViewComplaint(long complaintId, long userId) {
        if (!complaintRepository.existsByIdAndBuyerId(complaintId, userId)) {
            throw new AccessDenialException(ExceptionMessage.COMPLAINT_NO_RIGHTS_EXCEPTION.label);
        }
    }

    private void checkIfComplaintAlreadyExists(long userId, long productId) {
        if (complaintRepository.existsByBuyerIdAndProductId(userId, productId)) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }
}
