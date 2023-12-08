package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.CompliantResponseDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.server.model.complaint.Complaint;
import ru.softplat.main.server.model.seller.BankRequisites;

import java.util.List;

@Mapper
public interface ComplaintMapper {
    CompliantResponseDto complaintToComplaintDto(Complaint complaint);

    Complaint complaintDtoToComplaint(CompliantResponseDto complaintDto);

    default BankRequisitesResponseDto requisitesToDto(BankRequisites requisites) {
        if (requisites == null) return null;
        return new BankRequisitesResponseDto(requisites.getId(), requisites.getAccount());
    }

    default ComplaintListResponseDto toComplaintListResponseDto(List<CompliantResponseDto> complaints) {
        return ComplaintListResponseDto.builder().complaints(complaints).build();
    }
}
