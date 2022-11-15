package com.musinsa.orders.domain.refund;

import com.musinsa.orders.domain.order.Money;
import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refund")
@Entity
public class Refund {

  public enum RefundStatus {
    ACCEPTED, RETURNED, DELIVERED, IN_PROGRESS, COMPLETED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RefundStatus status;

  @Embedded
  private RefundReason reason;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "return_shipping_fee", nullable = false))
  private Money returnShippingFee;

  @Embedded
  private RefundLineItems refundLineItems;

  public Refund(
      Long orderId,
      RefundReason reason,
      Money returnShippingFee,
      List<RefundLineItem> refundLineItems
  ) {
    this(null, orderId, reason, returnShippingFee, refundLineItems);
  }

  public Refund(
      Long id,
      Long orderId,
      RefundReason reason,
      Money returnShippingFee,
      List<RefundLineItem> refundLineItems
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
    this.status = RefundStatus.ACCEPTED;
    this.reason = reason;
    this.returnShippingFee = returnShippingFee;
    this.refundLineItems = new RefundLineItems(refundLineItems);
  }

  public Long id() {
    return id;
  }

  public Long orderId() {
    return orderId;
  }

  public RefundStatus status() {
    return status;
  }

  public RefundReason reason() {
    return reason;
  }

  public Money returnShippingFee() {
    return returnShippingFee;
  }

  public List<RefundLineItem> getRefundLineItems() {
    return refundLineItems.refundLineItems();
  }

  public RefundLineItems refundLineItems() {
    return refundLineItems;
  }
}
