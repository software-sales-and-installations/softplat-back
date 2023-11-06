package ru.yandex.workshop.main.dto.buyer;

import lombok.*;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String telephone;
}
