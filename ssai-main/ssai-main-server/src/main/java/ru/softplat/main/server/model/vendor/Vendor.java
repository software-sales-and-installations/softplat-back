package ru.softplat.main.server.model.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import ru.softplat.main.dto.vendor.Country;
import ru.softplat.main.server.model.image.Image;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    Image image;
    @Enumerated(EnumType.STRING)
    Country country;
}
