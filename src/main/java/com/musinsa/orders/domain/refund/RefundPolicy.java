package com.musinsa.orders.domain.refund;

import com.musinsa.orders.domain.Money;
import com.musinsa.orders.domain.Order;
import java.util.List;

public class RefundPolicy {

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    return Money.from(2_500L);
  }

}
