package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintReasonRequest;
import ru.softplat.main.dto.compliant.CompliantResponseDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;
import ru.softplat.main.server.validation.ValidComplaintReasonRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Slf4j
public class ComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @GetMapping
    public ComplaintListResponseDto getComplaintListForAdmin() {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label);

        List<CompliantResponseDto> collect = complaintService.getAllComplaints()
                .stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());

        return complaintMapper.toComplaintListResponseDto(collect);
    }

    @PostMapping("/buyer/{userId}/{productId}/complaint")
    @ResponseStatus(HttpStatus.CREATED)
    public CompliantResponseDto createComplaint(@PathVariable Long userId,
                                                @PathVariable Long productId,
                                                @RequestParam @ValidComplaintReasonRequest ComplaintReasonRequest reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label, userId, productId);
        Complaint complaint = complaintService.createComplaint(userId, productId, reason);

        return complaintMapper.complaintToComplaintDto(complaint);
    }

    @GetMapping("/seller/{userId}/complaints")
    public ComplaintListResponseDto getComplaintListForSeller(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label, userId);

        List<CompliantResponseDto> collect = complaintService.getAllSellerComplaints(userId)
                .stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());

        return complaintMapper.toComplaintListResponseDto(collect);
    }
}
