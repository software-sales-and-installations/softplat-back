package ru.yandex.workshop.main.main.model.image;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "size")
    private Float size;
    @Column(name = "type")
    private String contentType;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] bytes;
}
