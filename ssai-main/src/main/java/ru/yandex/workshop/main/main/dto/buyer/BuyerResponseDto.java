package ru.yandex.workshop.main.main.dto.buyer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class BuyerResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String telephone;
}
