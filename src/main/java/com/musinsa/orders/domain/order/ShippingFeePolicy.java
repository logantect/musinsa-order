package com.musinsa.orders.domain.order;

import org.springframework.stereotype.Component;

@Component
public class ShippingFeePolicy {

  private static final Money FREE_SHIPPING_BASE_AMOUNT = Money.from(50_000L);
  public static final Money SHIPPING_FEE = Money.from(2_500L);
  public static final Money ROUND_TRIP_SHIPPING_FEE = Money.from(2_500L).times(2);

  public Money calculateShippingFee(Order order) {
    if (order.calculateTotalAmount().isGreaterThanOrEqual(FREE_SHIPPING_BASE_AMOUNT)) {
      return Money.ZERO;
    }
    return SHIPPING_FEE;
  }
}
