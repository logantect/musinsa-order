package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.domain.order.ShippingFeePolicy.ROUND_TRIP_SHIPPING_FEE;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class RefundPolicy {

  private final RefundRepository refundRepository;

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    RefundLineItems refundRequestLineItems = RefundLineItems.from(returnLineItemIds);
    List<RefundLineItem> refundedLineItems = getRefundedLineItems(order.id());

    validateNotExist(order, returnLineItemIds);
    validateAlreadyRefund(refundRequestLineItems, refundedLineItems);

    boolean fullRefund = isFullRefund(order, refundRequestLineItems.concat(refundedLineItems));

    if (order.isFreeShippingFee() && fullRefund) {
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

  private void validateAlreadyRefund(
      RefundLineItems refundRequestLineItems,
      List<RefundLineItem> refundedLineItems
  ) {
    if (ObjectUtils.isEmpty(refundedLineItems)) {
      return;
    }

    if (refundRequestLineItems.existRefundLineItem(refundedLineItems)) {
      throw new IllegalArgumentException("이미 환불진행 중이거나 환불완료된 주문 상품이 존재합니다");
    }
  }

  private boolean isFullRefund(Order order, RefundLineItems refundLineItems) {
    return Objects.equals(order.orderLineItems(),
        order.getOrderLineItems(refundLineItems.getOrderLineItemIds()));
  }

  private List<RefundLineItem> getRefundedLineItems(final Long orderId) {
    List<Refund> refundedLineItems = refundRepository.findByOrderId(orderId);
    return refundedLineItems.stream()
        .flatMap(refund -> refund.refundLineItems().stream())
        .collect(Collectors.toList());
  }

}
