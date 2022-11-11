package com.musinsa.orders.domain.order;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(
      name = "order_id",
      nullable = false,
      columnDefinition = "binary(16)",
      foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
  )
  private List<OrderLineItem> orderLineItems = List.of();

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

  public List<OrderLineItem> getOrderLineItems(List<Long> orderLineItemIds) {
    return orderLineItems.stream()
        .filter(orderLineItem -> orderLineItemIds.contains(orderLineItem.id()))
        .collect(Collectors.toList());
  }

}
