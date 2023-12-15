package ru.softplat.main.server.web.controller.complaint;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.dto.compliant.ComplaintStatus;
import ru.softplat.main.dto.compliant.ComplaintUpdateDto;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.complaint.ComplaintService;
import ru.softplat.main.server.service.product.ProductService;
import ru.softplat.main.server.web.controller.AbstractControllerTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ComplaintControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private ProductService productService;

    static Buyer buyer1, buyer2, buyer3;
    static Product product1, product2, product3;
    static Complaint complaint1, complaint2, complaint3, complaint4, complaint5;

    @BeforeEach
    void init() {
        product1 = productService.getAvailableProduct(1L);
        product2 = productService.getAvailableProduct(2L);
        product3 = productService.getAvailableProduct(3L);

        buyer1 = buyerService.getBuyer(1L);
        buyer2 = buyerService.getBuyer(2L);
        buyer3 = buyerService.getBuyer(3L);
    }

    static Stream<Arguments> complaintSearchBySellerId() {
        return Stream.of(
                Arguments.of(1L, List.of(5L, 4L, 3L)),
                Arguments.of(2L, List.of(2L)),
                Arguments.of(3L, List.of(1L))
        );
    }

    static Stream<Arguments> complaintSearchByProductId() {
        return Stream.of(
                Arguments.of(1L, List.of(5L, 4L, 3L)),
                Arguments.of(2L, List.of(2L)),
                Arguments.of(3L, List.of(1L))
        );
    }

    @Test
    void createComplaint_shouldUpdateProductComplaintCount_whenInitComplaints() {
        // given
        initComplaints();

        Product updatedProduct1 = productService.getAvailableProduct(product1.getId());
        Product updatedProduct2 = productService.getAvailableProduct(product2.getId());
        Product updatedProduct3 = productService.getAvailableProduct(product3.getId());

        long expectedComplaintCount1 = 3L;
        long expectedComplaintCount2 = 1L;
        long expectedComplaintCount3 = 1L;

        // when
        long actualProductComplaintCount1 = updatedProduct1.getComplaintCount();
        long actualProductComplaintCount2 = updatedProduct2.getComplaintCount();
        long actualProductComplaintCount3 = updatedProduct3.getComplaintCount();

        // then
        assertEquals(expectedComplaintCount1, actualProductComplaintCount1);
        assertEquals(expectedComplaintCount2, actualProductComplaintCount2);
        assertEquals(expectedComplaintCount3, actualProductComplaintCount3);
    }

    @Test
    void getAllComplaintsByAdmin_shouldReturnFiveComplaints_whenPostedByUsers() {
        // given
        initComplaints();
        int from = 0, size = 5;

        long expectCount = 5L;
        List<Complaint> expect = List.of(complaint5, complaint4, complaint3, complaint2, complaint1);

        // when
        long actualCount = complaintService.countAllComplaintsForAdmin();
        List<Complaint> actual = complaintService.getAllComplaints(from, size);

        // then
        assertEquals(expectCount, actualCount);
        performComplaintAssertions(expect, actual);
    }

    @ParameterizedTest
    @MethodSource("complaintSearchBySellerId")
    @SneakyThrows
    void getAllSellerComplaints_shouldReturnComplaints_whenPassedSellerIds(Long sellerId,
                                                                           List<Long> expectedComplaints) {
        // given
        initComplaints();
        List<ComplaintResponseDto> expect = getComplaintsBySeller(sellerId, expectedComplaints);

        // when
        List<ComplaintResponseDto> actual = getComplaintsBySellers(sellerId).getComplaints();

        // then
        performAssertionsForDto(expect, actual);
        assertEquals(expect.size(), actual.size());
    }

    @ParameterizedTest
    @MethodSource("complaintSearchByProductId")
    @SneakyThrows
    void getAllSellerComplaints_shouldReturnComplaints_whenPassedProductIds(Long productId,
                                                                            List<Long> expectedComplaints) {
        // given
        initComplaints();
        long sellerId = getProductResponseDto(productId).getSeller().getId();
        List<ComplaintResponseDto> expect = getComplaintsBySeller(sellerId, expectedComplaints);

        // when
        List<ComplaintResponseDto> actual = getComplaintsByProducts(sellerId, productId).getComplaints();

        // then
        performAssertionsForDto(expect, actual);
        assertEquals(expect.size(), actual.size());
    }

    @Test
    void sendProductOnModerationByAdmin_shouldUpdateComplaintStatus_whenAdminStatusReview() {
        // given
        initComplaints();
        ComplaintUpdateDto updateDto = ComplaintUpdateDto.builder().status(ComplaintStatus.REVIEW).comment("comment").build();

        // then
        ComplaintResponseDto response = sendProductOnModerationByAdmin(1L, updateDto);
        ComplaintStatus actualStatus = response.getComplaintStatus();

        assertEquals(updateDto.getStatus(), actualStatus);
    }

    @Test
    void sendProductOnModerationByAdmin_shouldUpdateComplaintStatus_whenAdminStatusClosed() {
        // given
        initComplaints();
        ComplaintUpdateDto updateDto = ComplaintUpdateDto.builder().status(ComplaintStatus.CLOSED).comment("complaint closed").build();
        long expectedCount = 2L;

        // when
        ComplaintResponseDto response = sendProductOnModerationByAdmin(5L, updateDto);
        ComplaintStatus actualStatus = response.getComplaintStatus();

        long productId = response.getProduct().getId();
        long actualComplaintCount = productService.getAvailableProduct(productId).getComplaintCount();

        // then
        assertEquals(expectedCount, actualComplaintCount);
        assertEquals(updateDto.getStatus(), actualStatus);
    }

    @Test
    void updateProductComplaintsBySeller_shouldUpdateAllComplaints_whenProduct3IsUpdated() {
        // given
        initComplaints();
        long productId = product3.getId();

        // when
        complaintService.updateProductComplaintsBySeller(productId);

        // then
        List<Complaint> actualComplaints = complaintService.getAllProductComplaints(productId, 0, 5);

        for (Complaint c : actualComplaints) {
            assertEquals(ComplaintStatus.REVIEW, c.getComplaintStatus());
        }
    }

    @SneakyThrows
    List<ComplaintResponseDto> getComplaintsBySeller(long userId, List<Long> complaintIds) {
        List<ComplaintResponseDto> response = new ArrayList<>();
        for (Long id : complaintIds) response.add(getComplaintBySeller(userId, id));
        return response;
    }

    void initComplaints() {
        complaint1 = complaintService.createComplaint(buyer3.getId(), product3.getId(), ComplaintReason.SELLER_FRAUD);
        complaint2 = complaintService.createComplaint(buyer3.getId(), product2.getId(), ComplaintReason.SELLER_FRAUD);
        complaint3 = complaintService.createComplaint(buyer3.getId(), product1.getId(), ComplaintReason.SELLER_FRAUD);
        complaint4 = complaintService.createComplaint(buyer2.getId(), product1.getId(), ComplaintReason.SELLER_FRAUD);
        complaint5 = complaintService.createComplaint(buyer1.getId(), product1.getId(), ComplaintReason.SELLER_FRAUD);
    }

    void performComplaintAssertions(List<Complaint> actual, List<Complaint> expect) {
        assertEquals(expect.size(), actual.size());

        for (int i = 0; i < expect.size(); i++) {
            Complaint expectComplaint = expect.get(i);
            Complaint actualComplaint = actual.get(i);
            assertEquals(expectComplaint.getId(), actualComplaint.getId());
            assertEquals(expectComplaint.getComplaintStatus(), actualComplaint.getComplaintStatus());
            assertEquals(expectComplaint.getReason(), actualComplaint.getReason());
            assertEquals(expectComplaint.getProduct().getId(), actualComplaint.getProduct().getId());
            assertEquals(expectComplaint.getBuyer().getId(), actualComplaint.getBuyer().getId());
        }
    }

    void performAssertionsForDto(List<ComplaintResponseDto> actual, List<ComplaintResponseDto> expect) {
        assertEquals(expect.size(), actual.size());

        for (int i = 0; i < expect.size(); i++) {
            ComplaintResponseDto expectComplaint = expect.get(i);
            ComplaintResponseDto actualComplaint = actual.get(i);
            assertEquals(expectComplaint.getId(), actualComplaint.getId());
            assertEquals(expectComplaint.getReason(), actualComplaint.getReason());
            assertEquals(expectComplaint.getProduct().getId(), actualComplaint.getProduct().getId());
            assertEquals(expectComplaint.getBuyer().getId(), actualComplaint.getBuyer().getId());
        }
    }

    @SneakyThrows
    public ComplaintListResponseDto getComplaintsBySellers(long userId) {
        MvcResult result = mockMvc.perform(get("/complaint/seller")
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .param("minId", "0")
                        .param("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ComplaintListResponseDto.class);
    }

    @SneakyThrows
    public ComplaintListResponseDto getComplaintsByProducts(long userId, long productId) {
        MvcResult result = mockMvc.perform(get("/complaint/seller/{productId}/product", productId)
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .param("minId", "0")
                        .param("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ComplaintListResponseDto.class);
    }

    @SneakyThrows
    public ComplaintResponseDto sendProductOnModerationByAdmin(long complaintId, ComplaintUpdateDto updateDto) {
        MvcResult result = mockMvc.perform(patch("/complaint/admin/{complaintId}", complaintId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ComplaintResponseDto.class);
    }
}