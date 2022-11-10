package com.musinsa.orders.infra;

import com.musinsa.orders.domain.Order;
import com.musinsa.orders.domain.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, Long> {

}
