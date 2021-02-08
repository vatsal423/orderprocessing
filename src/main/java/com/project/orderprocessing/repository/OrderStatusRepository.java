package com.project.orderprocessing.repository;

import com.project.orderprocessing.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, UUID> {

    @Query("select o from OrderStatus o where o.name = :name")
    OrderStatus getStatusByName(@Param("name") String orderName);
}
