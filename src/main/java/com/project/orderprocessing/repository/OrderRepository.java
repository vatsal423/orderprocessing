package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
}
