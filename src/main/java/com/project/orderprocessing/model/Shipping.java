package com.project.orderprocessing.model;

import com.project.orderprocessing.model.Orders;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shipping")
@Data
public class Shipping {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "shipping_type",nullable = false)
    private String shippingType;

    @OneToOne(mappedBy = "shipping")
    private Orders orders;
}
