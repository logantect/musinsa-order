package com.musinsa.orders.domain.order;

import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLineItems orderLineItems;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "shipping_fee", nullable = false))
  private Money shippingFee;

  protected Order() {

  }

  public Order(List<OrderLineItem> orderLineItems, ShippingFeePolicy shippingFeePolicy) {
    this(null, orderLineItems, shippingFeePolicy);
  }

  public Order(Long id, List<OrderLineItem> orderLineItems, ShippingFeePolicy shippingFeePolicy) {
    this.id = id;
    this.orderLineItems = new OrderLineItems(orderLineItems);
    this.shippingFee = shippingFeePolicy.calculateShippingFee(this);
  }

  public List<OrderLineItem> orderLineItems() {
    return Collections.unmodifiableList(orderLineItems.orderLineItems());
  }

  public Money calculateTotalAmount() {
    return orderLineItems.calculateTotalAmount();
  }

  public Money shippingFee() {
    return shippingFee;
  }

  public List<OrderLineItem> getOrderLineItems(List<Long> orderLineItemIds) {
    return Collections.unmodifiableList(orderLineItems.getOrderLineItems(orderLineItemIds));
  }

  public boolean isFreeShippingFee() {
    return shippingFee.equals(Money.ZERO);
  }
}
