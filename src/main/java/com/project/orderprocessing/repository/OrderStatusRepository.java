package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, UUID> {
}
