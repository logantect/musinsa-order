package com.musinsa.orders.domain;

import java.util.List;

public class ExchangePolicy {

  private static final Money RETURN_SHIPPING_FEE = Money.from(5_000L);

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    validateNotExist(order, returnLineItemIds);
    return RETURN_SHIPPING_FEE;
  }

  private void validateNotExist(Order order, List<Long> returnLineItemIds) {
    List<OrderLineItem> returnLineItems = order.getOrderLineItems(returnLineItemIds);
    if (returnLineItems.size() != returnLineItemIds.size()) {
      throw new IllegalArgumentException("교환할 주문 상품이 존재하지 않습니다.");
    }
  }

}
