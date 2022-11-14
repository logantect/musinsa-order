package com.musinsa.orders.domain.exchange;

import com.musinsa.orders.domain.order.Money;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "exchange")
@Entity
public class Exchange {

  public enum ExchangeStatus {
    ACCEPTED, RETURNED, DELIVERED, IN_PROGRESS, EXCHANGE_DELIVERING, EXCHANGE_DELIVERED, COMPLETED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ExchangeStatus status;

  @Embedded
  private ExchangeReason reason;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "return_shipping_fee", nullable = false))
  private Money returnShippingFee;

  @ElementCollection
  @CollectionTable(name = "refund_line_item", joinColumns = @JoinColumn(name = "refund_id"))
  private List<ExchangeLineItem> exchangeLineItems = List.of();

  public Exchange(
      Long orderId,
      ExchangeReason reason,
      Money returnShippingFee,
      List<ExchangeLineItem> exchangeLineItems
  ) {
    this(null, orderId, reason, returnShippingFee, exchangeLineItems);
  }

  public Exchange(
      Long id,
      Long orderId,
      ExchangeReason reason,
      Money returnShippingFee,
      List<ExchangeLineItem> exchangeLineItems
  ) {
    if (Objects.isNull(orderId)) {
      throw new IllegalArgumentException("Order ID does not allow null");
    }

    if (Objects.isNull(reason)) {
      throw new IllegalArgumentException("Reason does not allow null");
    }

    if (Objects.isNull(returnShippingFee)) {
      throw new IllegalArgumentException("Return shipping fee does not allow null");
    }

    this.id = id;
    this.orderId = orderId;
    this.status = ExchangeStatus.ACCEPTED;
    this.reason = reason;
    this.returnShippingFee = returnShippingFee;
    this.exchangeLineItems = exchangeLineItems;
  }

  public Long id() {
    return id;
  }

  public Long orderId() {
    return orderId;
  }

  public ExchangeStatus status() {
    return status;
  }

  public ExchangeReason reason() {
    return reason;
  }

  public Money returnShippingFee() {
    return returnShippingFee;
  }

  public List<ExchangeLineItem> exchangeLineItems() {
    return Collections.unmodifiableList(exchangeLineItems);
  }
}
