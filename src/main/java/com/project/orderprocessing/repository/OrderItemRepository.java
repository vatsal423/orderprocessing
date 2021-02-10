package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItems, UUID> {
}
