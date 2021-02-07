package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payments, UUID> {
}
