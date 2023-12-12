package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.service.complaint.ComplaintService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Slf4j
public class BuyerComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ComplaintResponseDto createComplaint(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long productId,
                                                @RequestParam ComplaintReason reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label, userId, productId);
        Complaint complaint = complaintService.createComplaint(userId, productId, reason);
        return complaintMapper.complaintToComplaintDto(complaint);
    }
}
