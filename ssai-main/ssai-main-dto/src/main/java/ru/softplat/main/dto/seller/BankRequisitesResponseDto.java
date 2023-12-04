package ru.softplat.main.dto.seller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
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
