package com.musinsa.orders;

import com.musinsa.orders.domain.Order;
import com.musinsa.orders.domain.OrderLineItem;
import java.util.List;
import java.util.Random;

public class Fixtures {

  public static Order createOrder(List<OrderLineItem> orderLineItems) {
    return createOrder(new Random().nextLong(), orderLineItems);
  }

  public static Order createOrder(long id, List<OrderLineItem> orderLineItems) {
    return new Order(
        id,
        orderLineItems
    );
  }

  public static OrderLineItem createOrderLineItem(long productId, String name, long price) {
    return new OrderLineItem(new Random().nextLong(), productId, name, price);
  }
}
