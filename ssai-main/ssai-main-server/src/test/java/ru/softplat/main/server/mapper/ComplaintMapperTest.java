package ru.softplat.main.server.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;

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
    @Disabled
    void testComplaintToComplaintDto() {
        Complaint complaint = Complaint.builder()
                .id(1L)
                .buyer(Buyer.builder().id(2L).build())
                .product(Product.builder().id(3L).build())
                .reason(ComplaintReason.PIRATED_SOFTWARE)
                .createdAt(LocalDateTime.now())
                .build();

        ComplaintResponseDto complaintDto = complaintMapper.complaintToComplaintDto(complaint);
        assertNotNull(complaintDto);
        assertEquals(complaint.getReason(), complaintDto.getReason());
    }

    @Test
    void testComplaintDtoToComplaint() {
        ComplaintResponseDto complaintDto = ComplaintResponseDto.builder()
                .reason(ComplaintReason.SELLER_FRAUD)
                .buyer(BuyerResponseDto.builder().build())
                .product(ProductResponseDto.builder().id(3L).build())
                .createdAt(LocalDateTime.now())
                .build();

        Complaint complaint = complaintMapper.complaintDtoToComplaint(complaintDto);

        assertNotNull(complaint);
        assertEquals(ComplaintReason.SELLER_FRAUD, complaint.getReason());
    }

    @Test
    void testToComplaintListResponseDto() {
        List<ComplaintResponseDto> complaints = Collections.singletonList(
                ComplaintResponseDto.builder()
                        .reason(ComplaintReason.SOFTWARE_NOT_WORKING)
                        .buyer(BuyerResponseDto.builder().id(2L).build())
                        .product(ProductResponseDto.builder().id(3L).build())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        long count = 1L;

        ComplaintListResponseDto responseDto = complaintMapper.toComplaintListResponseDto(complaints, count);

        assertNotNull(responseDto);
        assertEquals(complaints.size(), responseDto.getComplaints().size());
        assertEquals(complaints.get(0).getReason(), responseDto.getComplaints().get(0).getReason());
    }
}