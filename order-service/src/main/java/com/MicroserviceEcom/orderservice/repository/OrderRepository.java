package com.MicroserviceEcom.orderservice.repository;

import com.MicroserviceEcom.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
