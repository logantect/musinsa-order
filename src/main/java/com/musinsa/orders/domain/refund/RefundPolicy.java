package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.domain.order.ShippingFeePolicy.ROUND_TRIP_SHIPPING_FEE;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefundPolicy {

  private final RefundRepository refundRepository;

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    boolean freeShippingFee = order.isFreeShippingFee();
    boolean fullRefund = isFullRefund(order,
        concat(getRefundedLineItemIds(order.id()), returnLineItemIds));

    if (freeShippingFee && fullRefund) {
      return ROUND_TRIP_SHIPPING_FEE;
    }
    return ShippingFeePolicy.SHIPPING_FEE;
  }

  private boolean isFullRefund(Order order, List<Long> returnLineItemIds) {
    return Objects.equals(order.orderLineItems(), order.getOrderLineItems(returnLineItemIds));
  }

  private List<Long> concat(List<Long> refundedLineItemIds, List<Long> returnLineItemIds) {
    return Stream.concat(
        refundedLineItemIds.stream(),
        returnLineItemIds.stream()
    ).toList();
  }

  private List<Long> getRefundedLineItemIds(final Long orderId) {
    List<Refund> refundedLineItems = refundRepository.findByOrderId(orderId);
    return refundedLineItems.stream()
        .flatMap(refund -> refund.refundLineItems().stream())
        .map(RefundLineItem::orderLineItemId)
        .collect(Collectors.toList());
  }

}
