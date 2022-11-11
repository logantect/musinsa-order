package com.musinsa.orders.domain.refund;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import java.util.List;

public class RefundPolicy {

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    return Money.from(2_500L);
  }

}
