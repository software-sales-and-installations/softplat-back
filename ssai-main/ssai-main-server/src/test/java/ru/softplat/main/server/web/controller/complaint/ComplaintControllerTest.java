package ru.softplat.main.server.web.controller.complaint;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.softplat.main.dto.compliant.ComplaintReasonRequest;
import ru.softplat.main.dto.compliant.CompliantResponseDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplaintService complaintService;

    @MockBean
    private ComplaintMapper complaintMapper;

    @Test
    @SneakyThrows
    void testCreateComplaint_whenValidReason_thenReturnComplaintDto() {
        ComplaintReasonRequest reason = ComplaintReasonRequest.SELLER_FRAUD;
        CompliantResponseDto complaintDto = CompliantResponseDto.builder()
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();
        Complaint complaint = new Complaint();
        when(complaintService.createComplaint(anyLong(), anyLong(), any(ComplaintReasonRequest.class))).thenReturn(complaint);
        when(complaintMapper.complaintToComplaintDto(any(Complaint.class))).thenReturn(complaintDto);

        mockMvc.perform(post("/complaint/buyer/{userId}/{productId}/complaint", 1L, 1L)
                        .param("reason", reason.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reason").value(reason.toString()));
    }

    @Test
    @SneakyThrows
    void testCreateComplaint_whenInvalidReason_thenReturnBadRequest() {
        String invalidReason = "INVALID_REASON";

        mockMvc.perform(post("/complaint/buyer/{userId}/{productId}/complaint", 1L, 1L)
                        .param("reason", invalidReason))
                .andExpect(status().isBadRequest());
    }
}