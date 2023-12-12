package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint/seller")
@Slf4j
public class SellerComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @GetMapping
    public ComplaintListResponseDto getComplaintListForSeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                                              @RequestParam int minId,
                                                              @RequestParam int pageSize) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label, userId);
        List<Complaint> complaintList = complaintService.getAllSellerComplaints(userId, minId, pageSize);
        List<ComplaintResponseDto> response = complaintList.stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());
        long count = complaintService.countAllComplaintsBySellerId(userId);
        return complaintMapper.toComplaintListResponseDto(response, count);
    }

    @GetMapping("/{productId}/product")
    public ComplaintListResponseDto getComplaintsForProductBySeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                    @PathVariable long productId,
                                                                    @RequestParam int minId,
                                                                    @RequestParam int pageSize) {
        log.info(LogMessage.TRY_GET_PRODUCT_COMPLAINTS.label);
        complaintService.checkSellerRightToViewComplaints(userId, productId);
        List<Complaint> complaintList = complaintService.getAllProductComplaints(productId, minId, pageSize);
        List<ComplaintResponseDto> response = complaintList.stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());
        long count = complaintService.countAllByProductId(productId);
        return complaintMapper.toComplaintListResponseDto(response, count);
    }

    @GetMapping("/{complaintId}")
    public ComplaintResponseDto getComplaintById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long complaintId) {
        log.info(LogMessage.TRY_GET_COMPLAINT.label, complaintId);
        complaintService.checkSellerRightToViewComplaint(complaintId, userId);
        Complaint response = complaintService.getComplaintById(complaintId);
        return complaintMapper.complaintToComplaintDto(response);
    }
}
