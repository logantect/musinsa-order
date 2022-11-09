package com.musinsa.orders.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

  @Column(name = "id", columnDefinition = "binary(16)")
  @Id
  private Long id;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(
      name = "order_id",
      nullable = false,
      columnDefinition = "binary(16)",
      foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
  )
  private List<OrderLineItem> orderLineItems = List.of();

  protected Order() {

  }

  public Order(List<OrderLineItem> orderLineItems) {
    this(null, orderLineItems);
  }

  public Order(Long id, List<OrderLineItem> orderLineItems) {
    this.id = id;
    this.orderLineItems = orderLineItems;
  }

  public List<OrderLineItem> orderLineItems() {
    return Collections.unmodifiableList(orderLineItems);
  }
}
