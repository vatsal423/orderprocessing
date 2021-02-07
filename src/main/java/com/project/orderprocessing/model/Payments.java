package com.project.orderprocessing.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class Payments {
    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "payment_type_id",referencedColumnName = "id",nullable = false)
    private PaymentsType paymentsType;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id",nullable = false)
    private Orders orders;

    @Column(name = "addressline1",length = 100,nullable = false)
    private String addressLine1;

    @Column(name = "addressline2",length = 50)
    private String addressLine2;

    @Column(name = "city",length = 30,nullable = false)
    private String city;

    @Column(name = "state",length = 50,nullable = false)
    private String state;

    @Column(name = "zip",nullable = false)
    private int zip;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    @Column(name = "date",nullable = false)
    private Date date;

    @Column(name = "confirmation_number",length = 50,nullable = false)
    private String confirmationNumber;
}
