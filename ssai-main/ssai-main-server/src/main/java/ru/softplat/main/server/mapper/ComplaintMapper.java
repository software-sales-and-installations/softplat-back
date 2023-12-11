package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.server.model.complaint.Complaint;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {OrderMapper.class, BuyerMapper.class, SellerMapper.class})
@Component
public interface ComplaintMapper {
    ComplaintResponseDto complaintToComplaintDto(Complaint complaint);

    Complaint complaintDtoToComplaint(ComplaintResponseDto complaintDto);

//    default BankRequisitesResponseDto requisitesToDto(BankRequisites requisites) {
//        if (requisites == null) return null;
//        return new BankRequisitesResponseDto(requisites.getId(), requisites.getAccount());
//    }

    default ComplaintListResponseDto toComplaintListResponseDto(List<ComplaintResponseDto> complaints) {
        return ComplaintListResponseDto.builder().complaints(complaints).build();
    }
}
