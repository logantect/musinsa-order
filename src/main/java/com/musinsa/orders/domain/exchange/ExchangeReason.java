package com.musinsa.orders.domain.exchange;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class ExchangeReason {

  public enum ExchangeReasonType {
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
  private ExchangeReasonType reason;

  @Lob
  private String detailedReason;

  public ExchangeReason(ExchangeReasonType reason, String detailedReason) {
    if (Objects.isNull(reason)) {
      throw new IllegalArgumentException("Reason does not allow null");
    }
    this.reason = reason;
    this.detailedReason = detailedReason;
  }

}
