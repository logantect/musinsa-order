package com.musinsa.orders.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private Long id;

  @Embedded
  private OrderLineItems orderLineItems;

  protected Order() {

  }

  public Order(List<OrderLineItem> orderLineItems) {
    this(null, orderLineItems);
  }

  public Order(Long id, List<OrderLineItem> orderLineItems) {
    this.id = id;
    this.orderLineItems = new OrderLineItems(orderLineItems);
  }

  public List<OrderLineItem> orderLineItems() {
    return Collections.unmodifiableList(orderLineItems.orderLineItems());
  }

  public Money calculateTotalAmount() {
    return orderLineItems.calculateTotalAmount();
  }
}
