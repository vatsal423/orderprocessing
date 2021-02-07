package com.project.orderprocessing.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
public class OrderItems {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "quantity",nullable = false,columnDefinition = "integer default 1")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id",nullable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "item_id",referencedColumnName = "id",nullable = false)
    private Items items;
}
