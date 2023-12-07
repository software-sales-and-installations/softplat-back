package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.CompliantDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("complaint/admin")
@Slf4j
public class ComplaintControllerAdmin {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @GetMapping("/complaints")
    public ComplaintListResponseDto getComplaintListForAdmin(@RequestHeader("X-Sharer-User-Id") Long adminId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_ADMIN.label, adminId);

        List<CompliantDto> collect = complaintService.getAllComplaints()
                .stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());

        return complaintMapper.toComplaintListResponseDto(collect);
    }
}
