package com.musinsa.orders;

import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.ExchangeLineItem;
import com.musinsa.orders.domain.exchange.ExchangeReason;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundLineItem;
import com.musinsa.orders.domain.refund.RefundReason;
import java.util.List;
import java.util.Random;

public class Fixtures {

  public static Order createOrder(
      List<OrderLineItem> orderLineItems
  ) {
    return createOrder(new Random().nextLong(), orderLineItems, new ShippingFeePolicy());
  }

  public static Order createOrder(
      Long id,
      List<OrderLineItem> orderLineItems
  ) {
    return createOrder(
        id,
        orderLineItems,
        new ShippingFeePolicy()
    );
  }

  public static Order createOrder(
      Long id,
      List<OrderLineItem> orderLineItems,
      ShippingFeePolicy shippingFeePolicy
  ) {
    return new Order(
        id,
        orderLineItems,
        shippingFeePolicy
    );
  }

  public static OrderLineItem createOrderLineItem(Long productId, String name, Long price) {
    return createOrderLineItem(new Random().nextLong(), productId, name, price);
  }

  public static OrderLineItem createOrderLineItem(Long id, Long productId, String name,
      Long price) {
    return new OrderLineItem(id, productId, name, price);
  }

  public static Refund refund(
      Long orderId,
      RefundReason reason,
      Money returnShippingFee,
      List<RefundLineItem> refundLineItems
  ) {
    return refund(
        null,
        orderId,
        reason,
        returnShippingFee,
        refundLineItems
    );
  }

  public static Refund refund(
      Long id,
      Long orderId,
      RefundReason reason,
      Money returnShippingFee,
      List<RefundLineItem> refundLineItems
  ) {
    return new Refund(
        id,
        orderId,
        reason,
        returnShippingFee,
        refundLineItems
    );
  }

  public static RefundLineItem refundLineItem(Long orderLineItemId) {
    return new RefundLineItem(orderLineItemId);
  }

  public static Exchange exchange(
      Long orderId,
      ExchangeReason reason,
      Money returnShippingFee,
      List<ExchangeLineItem> exchangeLineItem
  ) {
    return exchange(
        null,
        orderId,
        reason,
        returnShippingFee,
        exchangeLineItem
    );
  }

  public static Exchange exchange(
      Long id,
      Long orderId,
      ExchangeReason reason,
      Money returnShippingFee,
      List<ExchangeLineItem> exchangeLineItem
  ) {
    return new Exchange(
        id,
        orderId,
        reason,
        returnShippingFee,
        exchangeLineItem
    );
  }

  public static ExchangeLineItem exchangeLineItem(Long orderLineItemId) {
    return new ExchangeLineItem(orderLineItemId);
  }
}
