package com.musinsa.orders.domain.refund;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class RefundPolicy {

  private final RefundRepository refundRepository;

  public Money calculateReturnShippingFee(Order order, List<Long> returnLineItemIds) {
    boolean freeShippingFee = order.isFreeShippingFee();
    boolean fullRefund = isFullRefund(order,
        concat(getRefundedLineItemIds(order.id()), returnLineItemIds));

    if (freeShippingFee && fullRefund) {
      return Money.from(5_000L);
    }
    return Money.from(2_500L);
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
