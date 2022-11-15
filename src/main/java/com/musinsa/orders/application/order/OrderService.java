package com.musinsa.orders.application.order;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.musinsa.orders.application.order.OrderDtos.OrderRequest;
import com.musinsa.orders.application.order.OrderDtos.OrderResponse;
import com.musinsa.orders.application.refund.RefundDtos.RefundRequest;
import com.musinsa.orders.application.refund.RefundDtos.RefundResponse;
import com.musinsa.orders.domain.exchange.ExchangePolicy;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderRepository;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundLineItem;
import com.musinsa.orders.domain.refund.RefundLineItems;
import com.musinsa.orders.domain.refund.RefundPolicy;
import com.musinsa.orders.domain.refund.RefundRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final RefundPolicy refundPolicy;
  private final ExchangePolicy exchangePolicy;
  private final ShippingFeePolicy shippingFeePolicy;

  @Transactional
  public OrderResponse placeOrder(OrderRequest dto) {
    Order order = new Order(dto.toOrderLineItems(), shippingFeePolicy);
    return new OrderResponse(orderRepository.save(order));
  }

  public OrderResponse getOrder(final Long orderId) {
    Order foundOrder = orderRepository.findById(orderId)
        .orElseThrow(IllegalArgumentException::new);
    return new OrderResponse(foundOrder);
  }

  public Money calculateExchangeShippingFee(final Long orderId, List<Long> returnLineItemIds) {
    Order foundOrder = orderRepository.findById(orderId)
        .orElseThrow(IllegalAccessError::new);
    return exchangePolicy.calculateReturnShippingFee(foundOrder, returnLineItemIds);
  }

  public Money calculateRefundShippingFee(final Long orderId, List<Long> returnLineItemIds) {
    Order foundOrder = orderRepository.findById(orderId)
        .orElseThrow(IllegalAccessError::new);
    return refundPolicy.calculateReturnShippingFee(foundOrder, returnLineItemIds);
  }

}
