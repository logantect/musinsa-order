package com.musinsa.orders.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {

  @Column(name = "price", nullable = false)
  private long amount;

  public static Price from(long amount) {
    return new Price(amount);
  }

  private Price(long amount) {
    if (amount < 0) {
      throw new IllegalArgumentException();
    }
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Price price = (Price) o;
    return Objects.equals(amount, price.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }

  public String toString() {
    return amount + "원";
  }
}