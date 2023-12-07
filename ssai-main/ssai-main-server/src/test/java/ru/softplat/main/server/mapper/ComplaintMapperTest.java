package ru.softplat.main.server.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.CompliantDto;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;
import ru.softplat.main.dto.user.response.SellerResponseDto;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.complaint.ComplaintReason;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.seller.BankRequisites;
import ru.softplat.main.server.model.seller.Seller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComplaintMapperTest {

    private ComplaintMapper complaintMapper;

    @BeforeEach
    void setUp() {
        complaintMapper = Mappers.getMapper(ComplaintMapper.class);
    }

    @Test
    void testComplaintToComplaintDto() {
        Complaint complaint = Complaint.builder()
                .id(1L)
                .buyer(Buyer.builder().id(2L).build())
                .product(Product.builder().id(3L).build())
                .seller(Seller.builder().id(4L).build())
                .reason(ComplaintReason.PIRATED_SOFTWARE)
                .createdAt(LocalDateTime.now())
                .build();

        CompliantDto complaintDto = complaintMapper.complaintToComplaintDto(complaint);
        assertNotNull(complaintDto);
        assertEquals(complaint.getReason().toString(), complaintDto.getReason());
    }

    @Test
    void testComplaintDtoToComplaint() {
        CompliantDto complaintDto = CompliantDto.builder()
                .reason(ComplaintReason.SELLER_FRAUD.name())
                .buyer(BuyerResponseDto.builder().build())
                .product(ProductResponseDto.builder().id(3L).build())
                .seller(SellerResponseDto.builder().id(4L).build())
                .createdAt(LocalDateTime.now())
                .build();

        Complaint complaint = complaintMapper.complaintDtoToComplaint(complaintDto);

        assertNotNull(complaint);
        assertEquals(ComplaintReason.SELLER_FRAUD, complaint.getReason());
    }

    @Test
    void testRequisitesToDto() {
        BankRequisites requisites = BankRequisites.builder()
                .id(1L)
                .account("1234567890")
                .build();

        BankRequisitesResponseDto requisitesDto = complaintMapper.requisitesToDto(requisites);

        assertNotNull(requisitesDto);
        assertEquals(requisites.getId(), requisitesDto.getId());
        assertEquals(requisites.getAccount(), requisitesDto.getAccount());
    }

    @Test
    void testToComplaintListResponseDto() {
        List<CompliantDto> complaints = Collections.singletonList(
                CompliantDto.builder()
                        .reason(ComplaintReason.SOFTWARE_NOT_WORKING.getReason())
                        .buyer(BuyerResponseDto.builder().id(2L).build())
                        .product(ProductResponseDto.builder().id(3L).build())
                        .seller(SellerResponseDto.builder().id(4L).build())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        ComplaintListResponseDto responseDto = complaintMapper.toComplaintListResponseDto(complaints);

        assertNotNull(responseDto);
        assertEquals(complaints.size(), responseDto.getComplaints().size());
        assertEquals(complaints.get(0).getReason(), responseDto.getComplaints().get(0).getReason());
    }
}