package com.toyota.salesservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Sales class represents a sales entity in the database.
 */

@Entity
@Table(name = "sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "sales_number", unique = true, nullable = false)
    private String salesNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "sales_date")
    private LocalDateTime salesDate;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "money", columnDefinition = "numeric")
    private Double money;

    @Column(name = "change", columnDefinition = "numeric")
    private Double change;

    @OneToMany(mappedBy = "sales", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SalesItems> salesItemsList;
}
