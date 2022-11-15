package com.musinsa.orders.domain.order;

import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderLineItems {

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
  @JoinColumn(
      name = "order_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
  )
  private List<OrderLineItem> orderLineItems;

  public OrderLineItems(List<OrderLineItem> orderLineItems) {
    if (orderLineItems == null || orderLineItems.isEmpty()) {
      throw new IllegalArgumentException();
    }
    this.orderLineItems = orderLineItems;
  }

  public Money calculateTotalAmount() {
    return orderLineItems.stream()
        .map(OrderLineItem::price)
        .reduce(Money.ZERO, Money::plus);
  }

  public List<OrderLineItem> orderLineItems() {
    return Collections.unmodifiableList(orderLineItems);
  }

  public boolean existOrderLineItem(List<Long> orderLineItemIds) {
    return this.orderLineItems.stream().anyMatch(it -> orderLineItemIds.contains(it.id()));
  }

  public boolean allMatchOrderLineItem(List<Long> orderLineItemIds) {
    return this.orderLineItems.stream().allMatch(it -> orderLineItemIds.contains(it.id()));
  }

}
