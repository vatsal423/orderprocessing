package com.project.orderprocessing.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_status")
@Data
public class OrderStatus {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "name",nullable = false)
    private String name;

    @OneToOne(mappedBy = "orderStatus")
    private Orders orders;

}
