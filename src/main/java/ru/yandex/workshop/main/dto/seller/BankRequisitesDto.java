package ru.yandex.workshop.main.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BankRequisitesDto {
    Long id;
    @Pattern(regexp = "[0-9]{16}", message = "Номер счета должен содержать 16 цифр 0-9")
    @NotBlank
    private String account;
}
