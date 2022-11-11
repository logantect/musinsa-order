package com.musinsa.orders.domain.refund;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class RefundReason {

  public enum RefundReasonType {
    CHANGE_OF_MIND,
    WRONG_DELIVERY;

    public boolean isCustomerFault() {
      return this == CHANGE_OF_MIND;
    }

    public boolean isSellerFault() {
      return this == WRONG_DELIVERY;
    }
  }

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RefundReasonType reason;

  @Lob
  private String detailedReason;

  public RefundReason(RefundReasonType reason, String detailedReason) {
    if (Objects.isNull(reason)) {
        throw new IllegalArgumentException("Reason does not allow null");
    }
    this.reason = reason;
    this.detailedReason = detailedReason;
  }

}
