package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.PaymentsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentsTypeRepository extends JpaRepository<PaymentsType, UUID> {
}
