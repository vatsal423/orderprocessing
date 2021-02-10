package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customers, UUID> {
}
