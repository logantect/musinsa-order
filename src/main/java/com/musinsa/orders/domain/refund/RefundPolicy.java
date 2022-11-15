package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.domain.order.ShippingFeePolicy.ROUND_TRIP_SHIPPING_FEE;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
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
    List<Long> refundedLineItemIds = getRefundedLineItemIds(order.id());
    validateNotExist(order, returnLineItemIds);
    validateAlreadyRefund(refundedLineItemIds, returnLineItemIds);

    boolean freeShippingFee = order.isFreeShippingFee();
    boolean fullRefund = isFullRefund(order, concat(refundedLineItemIds, returnLineItemIds));

    if (freeShippingFee && fullRefund) {
      return ROUND_TRIP_SHIPPING_FEE;
    }
    return ShippingFeePolicy.SHIPPING_FEE;
  }

  private void validateNotExist(Order order, List<Long> returnLineItemIds) {
    List<OrderLineItem> returnLineItems = order.getOrderLineItems(returnLineItemIds);
    if (returnLineItems.size() != returnLineItemIds.size()) {
      throw new IllegalArgumentException("환불할 주문 상품이 존재하지 않습니다.");
    }
  }

  private static void validateAlreadyRefund(List<Long> refundedLineItemIds,
      List<Long> returnLineItemIds) {
    refundedLineItemIds.forEach(it -> {
      if (returnLineItemIds.contains(it)) {
        throw new IllegalArgumentException("이미 환불진행 중이거나 환불완료된 주문 상품입니다. 주문 상품 ID: " + it);
      }
    });
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
