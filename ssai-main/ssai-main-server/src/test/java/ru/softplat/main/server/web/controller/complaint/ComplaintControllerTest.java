package ru.softplat.main.server.web.controller.complaint;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.softplat.main.dto.compliant.CompliantDto;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        String reason = "Мошенничество со стороны продавца";
        CompliantDto complaintDto = CompliantDto.builder()
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();
        Complaint complaint = new Complaint();
        when(complaintService.createComplaint(anyLong(), anyLong(), any(String.class))).thenReturn(complaint);
        when(complaintMapper.complaintToComplaintDto(any(Complaint.class))).thenReturn(complaintDto);

        mockMvc.perform(post("/complaint/buyer/{userId}/{productId}/complaint", 1L, 1L)
                        .content(reason))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reason").value(reason));
    }

    @Test
    @SneakyThrows
    void testCreateComplaint_whenInvalidReason_thenReturnBadRequest() {
        String invalidReason = "INVALID_REASON";

        mockMvc.perform(post("/complaint/buyer/{userId}/{productId}/complaint", 1L, 1L)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(invalidReason))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof WrongConditionException))
                .andExpect(result -> assertEquals("Неверно указана причина жалобы.",
                        result.getResolvedException().getMessage()));
    }
}