package ru.softplat.main.dto.compliant;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintListResponseDto {
    List<ComplaintResponseDto> complaints;
    Long totalComplaints;
}
