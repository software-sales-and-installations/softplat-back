package ru.softplat.main.server.service.complaint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.repository.buyer.BuyerRepository;
import ru.softplat.main.server.repository.buyer.OrderRepository;
import ru.softplat.main.server.repository.complaint.ComplaintRepository;
import ru.softplat.main.server.repository.product.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ComplaintServiceImplTest {
    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ComplaintServiceImpl complaintService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllComplaints_whenComplaintsExist_thenReturnComplaints() {
        List<Complaint> complaints = List.of(mock(Complaint.class));
        when(complaintRepository.findAll()).thenReturn(complaints);

        List<Complaint> result = complaintService.getAllComplaints();

        assertThat(result).isEqualTo(complaints);
    }

    @Test
    void testGetAllComplaints_whenNoComplaints_thenReturnEmptyList() {
        when(complaintRepository.findAll()).thenReturn(Collections.emptyList());

        List<Complaint> result = complaintService.getAllComplaints();

        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllSellerComplaints_whenComplaintsExistForSeller_thenReturnComplaints() {
        Long sellerId = 1L;
        List<Complaint> complaints = List.of(mock(Complaint.class));
        when(complaintRepository.findAllBySeller_Id(sellerId)).thenReturn(complaints);

        List<Complaint> result = complaintService.getAllSellerComplaints(sellerId);

        assertThat(result).isEqualTo(complaints);
    }

    @Test
    void testGetAllSellerComplaints_whenNoComplaintsForSeller_thenReturnEmptyList() {
        Long sellerId = 1L;
        when(complaintRepository.findAllBySeller_Id(sellerId)).thenReturn(Collections.emptyList());

        List<Complaint> result = complaintService.getAllSellerComplaints(sellerId);

        assertThat(result).isEmpty();
    }

    @Test
    void testCreateComplaint_whenEntitiesExist_thenReturnComplaint() {
        Long userId = 1L;
        Long productId = 1L;
        String reason = "SELLER_FRAUD";
        Buyer buyer = mock(Buyer.class);
        Product product = mock(Product.class);
        when(buyerRepository.findById(userId)).thenReturn(Optional.of(buyer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.findOrderByBuyerIdAndProductId(userId, productId)).thenReturn(Optional.of(mock(Order.class)));
        when(product.getComplaintCount()).thenReturn(0L);
        when(complaintRepository.save(any(Complaint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Complaint result = complaintService.createComplaint(userId, productId, reason);

        verify(productRepository).save(product);
        verify(complaintRepository).save(any(Complaint.class));
        assertThat(result.getBuyer()).isEqualTo(buyer);
        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getReason().name()).isEqualTo(reason);
    }

    @Test
    void testCreateComplaint_whenUserDoesNotExist_thenThrowEntityNotFoundException() {
        Long userId = 1L;
        Long productId = 1L;
        String reason = "SELLER_FRAUD";
        when(buyerRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> complaintService.createComplaint(userId, productId, reason));
    }

    @Test
    void testCreateComplaint_whenProductDoesNotExist_thenThrowEntityNotFoundException() {
        Long userId = 1L;
        Long productId = 1L;
        String reason = "SELLER_FRAUD";
        when(buyerRepository.findById(userId)).thenReturn(Optional.of(mock(Buyer.class)));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> complaintService.createComplaint(userId, productId, reason));
    }

    @Test
    void testCreateComplaint_whenOrderDoesNotExist_thenThrowEntityNotFoundException() {
        Long userId = 1L;
        Long productId = 1L;
        String reason = "SELLER_FRAUD";
        when(buyerRepository.findById(userId)).thenReturn(Optional.of(mock(Buyer.class)));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mock(Product.class)));
        when(orderRepository.findOrderByBuyerIdAndProductId(userId, productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> complaintService.createComplaint(userId, productId, reason));
    }
}