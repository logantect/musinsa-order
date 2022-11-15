package com.musinsa.orders.domain.refund;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.musinsa.orders.domain.order.Order;
import java.util.List;

public record RefundRequestLineItems(
    RefundLineItems refundRequestLineItems,
    List<RefundLineItem> refundedLineItems
) {

  public RefundRequestLineItems {
    if (!isEmpty(refundedLineItems) && refundRequestLineItems.existRefundLineItem(
        refundedLineItems)) {
      throw new IllegalArgumentException("이미 환불진행 중이거나 환불완료된 주문 상품이 존재합니다");
    }
  }

  public RefundRequestLineItems(
      List<Long> refundRequestLineItemIds,
      List<RefundLineItem> refundedLineItems
  ) {
    this(RefundLineItems.from(refundRequestLineItemIds), refundedLineItems);
  }

  public boolean isFullRefund(Order order) {
    RefundLineItems refundLineItems = refundRequestLineItems.concat(refundedLineItems);
    return order.allMatchLineItems(refundLineItems.getOrderLineItemIds());
  }

}