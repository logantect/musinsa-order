package com.musinsa.orders.application.refund;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.Refund.RefundStatus;
import com.musinsa.orders.domain.refund.RefundLineItem;
import com.musinsa.orders.domain.refund.RefundReason;
import com.musinsa.orders.domain.refund.RefundReason.RefundReasonType;
import java.util.List;
import java.util.stream.Collectors;

public class RefundDtos {

  public record RefundRequest(
      Long orderId,
      RefundReason reason,
      Long returnShippingFee,
      List<RefundLineItemsRequest> refundLineItems
  ) {

    public Refund toEntity() {
      return new Refund(
          orderId,
          new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
          Money.from(returnShippingFee),
          refundLineItems.stream()
              .map(it -> new RefundLineItem(it.orderLineItemId))
              .collect(Collectors.toList())
      );
    }
  }

  public record RefundLineItemsRequest(
      Long orderLineItemId
  ) {

  }

  public record RefundResponse(
      Long id,
      Long orderId,
      RefundStatus status,
      RefundReason reason,
      Long returnShippingFee,
      List<RefundLineItemResponse> refundLineItems
  ) {

    public RefundResponse(Refund refund) {
      this(
          refund.id(),
          refund.orderId(),
          refund.status(),
          refund.reason(),
          refund.returnShippingFee().amount(),
          refund.getRefundLineItems().stream()
              .map(it -> new RefundLineItemResponse(it.orderLineItemId()))
              .collect(Collectors.toList())
      );
    }
  }

  public record RefundLineItemResponse(
      Long orderLineItemId
  ) {

  }
}
