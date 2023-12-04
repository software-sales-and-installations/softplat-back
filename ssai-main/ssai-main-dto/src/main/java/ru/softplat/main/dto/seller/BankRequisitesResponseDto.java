package ru.softplat.main.dto.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankRequisitesResponseDto {
    Long id;
    String inn;
    LegalForm legalForm;
    String account;
    String bik;
    String kpp;
    String ogrn;
    String ogrnip;
    String address;
}
