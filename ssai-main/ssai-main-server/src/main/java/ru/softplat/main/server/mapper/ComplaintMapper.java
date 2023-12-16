package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.compliant.ComplaintListResponseDto;
import ru.softplat.main.dto.compliant.ComplaintResponseDto;
import ru.softplat.main.server.model.complaint.Complaint;

import java.util.List;

@Mapper(uses = {ProductMapper.class, BuyerMapper.class})
@Component
public interface ComplaintMapper {
    ComplaintResponseDto complaintToComplaintDto(Complaint complaint);

    Complaint complaintDtoToComplaint(ComplaintResponseDto complaintResponseDto);

    default ComplaintListResponseDto toComplaintListResponseDto(List<ComplaintResponseDto> complaints, long totalComplaints) {
        return ComplaintListResponseDto.builder().complaints(complaints).totalComplaints(totalComplaints).build();
    }
}
