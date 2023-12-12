package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.dto.compliant.ComplaintStatus;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint/admin")
@Slf4j
public class AdminComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @GetMapping
    public ComplaintListResponseDto getComplaintListForAdmin(@RequestParam int minId,
                                                             @RequestParam int pageSize) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label);
        List<Complaint> complaintList = complaintService.getAllComplaints(minId, pageSize);
        List<ComplaintResponseDto> response = complaintList.stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());
        long count = complaintService.countAllComplaintsForAdmin();
        return complaintMapper.toComplaintListResponseDto(response, count);
    }

    @GetMapping("/{productId}/product")
    public ComplaintListResponseDto getComplaintsForProductByAdmin(@PathVariable long productId,
                                                                   @RequestParam int minId,
                                                                   @RequestParam int pageSize) {
        log.info(LogMessage.TRY_GET_PRODUCT_COMPLAINTS.label);
        List<Complaint> complaintList = complaintService.getAllProductComplaints(productId, minId, pageSize);
        List<ComplaintResponseDto> response = complaintList.stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());
        long count = complaintService.countAllByProductId(productId);
        return complaintMapper.toComplaintListResponseDto(response, count);
    }

    @GetMapping("/{complaintId}")
    public ComplaintResponseDto getComplaintByIdByAdmin(@PathVariable long complaintId) {
        log.info(LogMessage.TRY_GET_COMPLAINT.label, complaintId);
        Complaint response = complaintService.getComplaintById(complaintId);
        return complaintMapper.complaintToComplaintDto(response);
    }

    @PatchMapping("/{complaintId}")
    public ComplaintResponseDto sendProductOnModerationByAdmin(@PathVariable long complaintId,
                                                               @RequestParam ComplaintStatus status,
                                                               @RequestBody String comment) {
        log.info(LogMessage.TRY_SEND_PRODUCT_ON_MODERATION_BY_ADMIN.label);
        Complaint response = complaintService.updateComplaintByAdmin(complaintId, comment, status);
        return complaintMapper.complaintToComplaintDto(response);
    }
}
