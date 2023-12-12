package ru.softplat.main.server.model.complaint;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintStatus;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "complaint")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Enumerated(EnumType.STRING)
    ComplaintReason reason;

    @DateTimeFormat
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ComplaintStatus complaintStatus;

    @Column(name = "comment")
    String adminComment;
}