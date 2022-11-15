package com.musinsa.orders.infra.order;

import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {

  private final Map<Long, Order> store = new HashMap<>();

  @Override
  public Order save(Order order) {
    store.put(order.id(), order);
    return order;
  }

  @Override
  public Optional<Order> findById(Long id) {
    return Optional.empty();
  }
}
