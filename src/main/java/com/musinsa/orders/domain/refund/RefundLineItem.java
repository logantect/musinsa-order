package com.musinsa.orders.domain.refund;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RefundLineItem {

  @Column(nullable = false)
  private Long orderLineItemId;

  public RefundLineItem(Long orderLineItemId) {
    if (Objects.isNull(orderLineItemId)) {
      throw new IllegalArgumentException("OrderLineItem ID does not allow null");
    }
    this.orderLineItemId = orderLineItemId;
  }
}
