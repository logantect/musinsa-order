package com.musinsa.orders.infra.order;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import org.springframework.stereotype.Component;

@Component
public class AmountShippingFeePolicy implements ShippingFeePolicy {

  private static final Money FREE_SHIPPING_BASE_AMOUNT = Money.from(50_000L);
  private static final Money SHIPPING_FEE = Money.from(2_500L);

  @Override
  public Money calculateShippingFee(Order order) {
    if (order.calculateTotalAmount().isGreaterThanOrEqual(FREE_SHIPPING_BASE_AMOUNT)) {
      return Money.ZERO;
    }
    return SHIPPING_FEE;
  }
}
