package ru.softplat.main.dto.seller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.validation.New;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankRequisitesCreateUpdateDto {
    @Pattern(regexp = "[0-9]{20}", message = "Номер счета должен содержать 20 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать номер счета")
    String account;
    @Pattern(regexp = "[0-9]{10,12}", message = "ИНН должен содержать 10 или 12 (ИП) цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать ИНН")
    String inn;
    @NotNull(groups = {New.class}, message = "Выберите правовую форму")
    LegalForm legalForm;
    @Pattern(regexp = "[0-9]{9}", message = "БИК должен содержать 9 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать БИК")
    String bik;
    @Pattern(regexp = "[0-9]{10}", message = "КПП должен содержать 10 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать КПП")
    String kpp;
    @Pattern(regexp = "[0-9]{13}", message = "ОГРН должен содержать 13 цифр 0-9")
    String ogrn;
    @Pattern(regexp = "[0-9]{15}", message = "ОГРНИП должен содержать 15 цифр 0-9")
    String ogrnip;
    @Pattern(regexp = "^[a-zA-Zа-яА-Я-.,\\s]{5,500}$", message = "Адрес должен содержать не более 500 цифр 0-9")
    @NotBlank(groups = {New.class}, message = "Необходимо указать юридический адрес")
    String address;
}
