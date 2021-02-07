package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Items, UUID> {
}
