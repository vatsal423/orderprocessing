package com.project.orderprocessing.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "items")
@Data
public class Items {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @OneToMany(mappedBy = "items")
    private List<OrderItems> orderItemsList;

    @Column(name = "name",nullable =false)
    private String name;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "sin_no",nullable = false)
    private int sinNo;

    @Column(name = "description")
    private String description;
}
