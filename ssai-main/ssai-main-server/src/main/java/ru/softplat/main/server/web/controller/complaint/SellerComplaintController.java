package ru.softplat.main.server.web.controller.complaint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.dto.product.ProductCreateUpdateDto;
import ru.softplat.main.server.mapper.ComplaintMapper;
import ru.softplat.main.server.mapper.ProductMapper;
import ru.softplat.main.server.message.LogMessage;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.product.Product;
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
    private final ProductMapper productMapper;

    @GetMapping
    public ComplaintListResponseDto getComplaintListForSeller(@RequestHeader("X-Sharer-User-Id") long userId,
                                                              @RequestParam int minId,
                                                              @RequestParam int pageSize) {
        log.info(LogMessage.TRY_GET_ALL_COMPLAINTS_SELLER.label, userId);
        List<Complaint> complaintList = complaintService.getAllSellerComplaints(userId, minId, pageSize);
        List<ComplaintResponseDto> response = complaintList.stream()
                .map(complaintMapper::complaintToComplaintDto)
                .collect(Collectors.toList());
        return complaintMapper.toComplaintListResponseDto(response);
    }

    @GetMapping("/{productId}")
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
        return complaintMapper.toComplaintListResponseDto(response);
    }

    @GetMapping("/{complaintId}")
    public ComplaintResponseDto getComplaintById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long complaintId) {
        log.info(LogMessage.TRY_GET_COMPLAINT.label, complaintId);
        complaintService.checkSellerRightToViewComplaint(complaintId, userId);
        Complaint response = complaintService.getComplaintById(complaintId);
        return complaintMapper.complaintToComplaintDto(response);
    }

    // TODO возможно стоит отнести в обыкновенный контроллер продукта и туда передавать id жалобы
    @PatchMapping("{complaintId}/product/{productId}")
    public ComplaintResponseDto updateProductDueToComplaint(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @PathVariable long complaintId,
                                                            @PathVariable long productId,
                                                            @RequestBody ProductCreateUpdateDto updateDto) {
        log.info(LogMessage.TRY_SELLER_UPDATE_PRODUCT_DUE_TO_COMPLAINT.label);
        complaintService.checkSellerRightToViewComplaint(complaintId, userId);
        Product productForUpdate = productMapper.productDtoToProduct(updateDto);
        Complaint response = complaintService.updateProductDueToComplaint(complaintId, productId, productForUpdate);
        return complaintMapper.complaintToComplaintDto(response);
    }
}
