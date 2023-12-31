package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.dto.compliant.ComplaintUpdateDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint/admin")
public class AdminComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @GetMapping
    public ComplaintListResponseDto getComplaintListForAdmin(@RequestParam int minId,
                                                             @RequestParam int pageSize) {
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
        List<Complaint> complaintList = complaintService.getAllProductComplaints(productId, minId, pageSize);
        List<ComplaintResponseDto> response = complaintList.stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());
        long count = complaintService.countAllByProductId(productId);
        return complaintMapper.toComplaintListResponseDto(response, count);
    }

    @GetMapping("/{complaintId}")
    public ComplaintResponseDto getComplaintByIdByAdmin(@PathVariable long complaintId) {
        Complaint response = complaintService.getComplaintById(complaintId);
        return complaintMapper.complaintToComplaintDto(response);
    }

    @PatchMapping("/{complaintId}")
    public ComplaintResponseDto sendProductOnModerationByAdmin(@PathVariable long complaintId,
                                                               @RequestBody ComplaintUpdateDto updateDto) {
        Complaint response = complaintService.updateComplaintByAdmin(complaintId, updateDto);
        return complaintMapper.complaintToComplaintDto(response);
    }
}
