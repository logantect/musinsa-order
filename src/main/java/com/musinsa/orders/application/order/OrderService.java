package com.musinsa.orders.application.order;

import com.musinsa.orders.application.order.OrderDtos.OrderRequest;
import com.musinsa.orders.application.order.OrderDtos.OrderResponse;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderRepository;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ShippingFeePolicy shippingFeePolicy;

  @Transactional
  public OrderResponse placeOrder(OrderRequest dto) {
    Order order = new Order(dto.toOrderLineItems(), shippingFeePolicy);
    return new OrderResponse(orderRepository.save(order));
  }

  public OrderResponse getOrder(final Long orderId) {
    Order foundOrder = orderRepository.findById(orderId)
        .orElseThrow(IllegalAccessError::new);
    return new OrderResponse(foundOrder);
  }

}
