package com.musinsa.orders.domain.exchange;

import static com.musinsa.orders.domain.order.ShippingFeePolicy.ROUND_TRIP_SHIPPING_FEE;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExchangePolicy {

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    validateNotExist(order, returnLineItemIds);
    return ROUND_TRIP_SHIPPING_FEE;
  }

  private void validateNotExist(Order order, List<Long> returnLineItemIds) {
    if (!order.existOrderLineItems(returnLineItemIds)) {
      throw new IllegalArgumentException("교환할 주문 상품이 존재하지 않습니다.");
    }
  }

}
