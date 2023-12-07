package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.compliant.CompliantDto;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.complaint.ComplaintReason;
import ru.softplat.main.server.service.complaint.ComplaintService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("complaint/buyer")
@Slf4j
public class ComplaintControllerBuyer {
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @PostMapping("/{userId}/{productId}/complaint")
    @ResponseStatus(HttpStatus.CREATED)
    public CompliantDto createComplaint(@PathVariable Long userId,
                                        @PathVariable Long productId,
                                        @RequestBody String reason) {
        log.info(LogMessage.TRY_ADD_COMPLAINT_BUYER.label, userId, productId);

        if (!isValidComplaintReason(reason)) {
            throw new WrongConditionException(ExceptionMessage.COMPLAINT_REASON_EXCEPTION.label);
        }

        Complaint complaint = complaintService.createComplaint(userId, productId, reason);

        return complaintMapper.complaintToComplaintDto(complaint);
    }

    @GetMapping("/complaints")
    public List<String> getAllComplaintReasons() {
        List<String> complaintReasons = new ArrayList<>();

        for (ComplaintReason complaintReason : ComplaintReason.values()) {
            complaintReasons.add(complaintReason.getReason());
        }

        return complaintReasons;
    }


    private boolean isValidComplaintReason(String reason) {
        for (ComplaintReason complaintReason : ComplaintReason.values()) {
            if (complaintReason.getReason().equalsIgnoreCase(reason)) {
                return true;
            }
        }
        return false;
    }
}
