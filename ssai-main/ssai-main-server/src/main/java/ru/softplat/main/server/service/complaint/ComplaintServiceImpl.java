package ru.softplat.main.server.service.complaint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.dto.compliant.ComplaintReasonRequest;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.repository.buyer.BuyerRepository;
import ru.softplat.main.server.repository.buyer.OrderRepository;
import ru.softplat.main.server.repository.complaint.ComplaintRepository;
import ru.softplat.main.server.repository.product.ProductRepository;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;
    private final OrderRepository orderRepository;

    //можно установить page
    @Override
    @Transactional(readOnly = true)
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Complaint> getAllSellerComplaints(Long sellerId) {
        return complaintRepository.findAllBySeller_Id(sellerId);
    }

    @Override
    @Transactional
    public Complaint createComplaint(Long userId, Long productId, ComplaintReasonRequest reason) {
        Buyer buyer = buyerRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage
                .ENTITY_NOT_FOUND_EXCEPTION.getMessage(userId, Buyer.class)));

        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(
                ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(productId, Product.class)));

        Order order = orderRepository.findOrderByBuyerIdAndProductId(userId, productId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.NOT_VALID_COMPLAINT_PRODUCT_EXCEPTION.label));

        product.setComplaintCount(product.getComplaintCount() + 1);

        checkComplaintsMustBeLessThanTen(product);

        Complaint newComplaint = Complaint.builder()
                .seller(product.getSeller())
                .createdAt(now())
                .reason(reason)
                .product(product)
                .order(order)
                .buyer(buyer)
                .build();

        productRepository.save(product);

        return complaintRepository.save(newComplaint);
    }

     private void checkComplaintsMustBeLessThanTen(Product product) {
        if (product.getComplaintCount() >= 10) {
            product.setProductAvailability(Boolean.FALSE);
            product.setProductStatus(ProductStatus.REJECTED);
            productRepository.save(product);
        }
    }
}
