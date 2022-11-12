package com.musinsa.orders.domain.refund;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import java.util.List;
import java.util.Objects;

public class RefundPolicy {

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    List<OrderLineItem> returnLineItems = order.getOrderLineItems(returnLineItemIds);
    boolean freeShippingFee = order.isFreeShippingFee();
    boolean fullRefund = isFullRefund(order, returnLineItems);
    if (freeShippingFee) {
      if (fullRefund) {
        return Money.from(5_000L);
      } else {
        return Money.from(2_500L);
      }
    }
    return Money.from(2_500L);
  }

  private boolean isFullRefund(Order order, List<OrderLineItem> returnLineItems) {
    if (Objects.equals(order.orderLineItems(), returnLineItems)) {
      return true;
    }
    return false;
  }

}
