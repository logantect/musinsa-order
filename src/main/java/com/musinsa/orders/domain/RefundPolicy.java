package com.musinsa.orders.domain;

import java.util.List;

public class RefundPolicy {

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    return Money.from(2_500L);
  }

}
