package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.domain.order.ShippingFeePolicy.ROUND_TRIP_SHIPPING_FEE;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundPolicy {

  private final RefundRepository refundRepository;

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    validateNotExist(order, returnLineItemIds);
    RefundRequestLineItems refundRequestLineItems = new RefundRequestLineItems(
        returnLineItemIds,
        getRefundedLineItems(order.id())
    );

    if (order.isFreeShippingFee() && refundRequestLineItems.isFullRefund(order)) {
      return ROUND_TRIP_SHIPPING_FEE;
    }
    return ShippingFeePolicy.SHIPPING_FEE;
  }

  private void validateNotExist(Order order, List<Long> returnLineItemIds) {
    if (!order.existOrderLineItems(returnLineItemIds)) {
      throw new IllegalArgumentException("환불할 주문 상품이 존재하지 않습니다.");
    }
  }

  private List<RefundLineItem> getRefundedLineItems(final Long orderId) {
    List<Refund> refundedLineItems = refundRepository.findByOrderId(orderId);
    return refundedLineItems.stream()
        .flatMap(refund -> refund.getRefundLineItems().stream())
        .collect(Collectors.toList());
  }

}
