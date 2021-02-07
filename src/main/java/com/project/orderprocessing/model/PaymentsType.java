package com.project.orderprocessing.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "payments_type")
@Data
public class PaymentsType {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "payment_type",nullable = false)
    private String paymentType;

    @OneToOne(mappedBy = "paymentsType")
    private Payments payments;

}
