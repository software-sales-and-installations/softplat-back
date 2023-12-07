package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("complaint/seller")
@Slf4j
public class ComplaintControllerSeller {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @GetMapping("/{userId}/complaints")
    public ComplaintListResponseDto getComplaintListForSeller(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label, userId);

        List<CompliantDto> collect = complaintService.getAllSellerComplaints(userId)
                .stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());

        return complaintMapper.toComplaintListResponseDto(collect);
    }
}
