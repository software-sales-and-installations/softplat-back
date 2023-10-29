package ru.yandex.workshop.main.dto.buyer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class BuyerDto {
    @NotNull
    @Email
    @Size(min = 6, max = 30)
    private String email;
    @NotNull
    @Size(min = 2, max = 20)
    private String firstName;
    @NotNull
    @Size(min = 2, max = 20)
    private String lastName;
    @NotNull
    @Size(min = 10, max = 10)
    private String telephone;
}
