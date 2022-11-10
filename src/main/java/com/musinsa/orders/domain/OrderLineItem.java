package com.musinsa.orders.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long productId;

  @Embedded
  private ProductName name;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
  private Money price;

  protected OrderLineItem() {

  }

  public OrderLineItem(Long productId, String name, Long price) {
    this(null, productId, name, price);
  }

  public OrderLineItem(Long id, Long productId, String name, Long price) {
    this.id = id;
    this.productId = productId;
    this.name = ProductName.from(name);
    this.price = Money.from(price);
  }

  public Money price() {
    return price;
  }
}
