package com.musinsa.orders;

import com.musinsa.orders.domain.Order;
import com.musinsa.orders.domain.OrderLineItem;
import com.musinsa.orders.domain.ShippingFeePolicy;
import com.musinsa.orders.infra.AmountShippingFeePolicy;
import java.util.List;
import java.util.Random;

public class Fixtures {

  public static Order createOrder(
      List<OrderLineItem> orderLineItems
  ) {
    return createOrder(new Random().nextLong(), orderLineItems, new AmountShippingFeePolicy());
  }

  public static Order createOrder(
      Long id,
      List<OrderLineItem> orderLineItems
  ) {
    return createOrder(
        id,
        orderLineItems,
        new AmountShippingFeePolicy()
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

  public static OrderLineItem createOrderLineItem(long productId, String name, long price) {
    return new OrderLineItem(new Random().nextLong(), productId, name, price);
  }
}
