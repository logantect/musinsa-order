package com.musinsa.orders.domain;

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

  public List<OrderLineItem> orderLineItems() {
    return Collections.unmodifiableList(orderLineItems);
  }
}
