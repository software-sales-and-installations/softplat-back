package ru.softplat.main.server.model.seller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.seller.LegalForm;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requisite")
public class BankRequisites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String inn;
    @Column(name = "legal_form")
    @Enumerated(EnumType.STRING)
    LegalForm legalForm;
    String account;
    String bik;
    String kpp;
    String ogrn;
    String ogrnip;
    String address;
}
