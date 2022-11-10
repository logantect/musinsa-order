package com.musinsa.orders.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Money {

  public static final Money ZERO = Money.from(0);

  private long amount;

  public static Money from(long amount) {
    return new Money(amount);
  }

  private Money(long amount) {
    if (amount < 0) {
      throw new IllegalArgumentException();
    }
    this.amount = amount;
  }

  public Money plus(Money amount) {
    return new Money(this.amount + amount.amount);
  }

  public boolean isGreaterThanOrEqual(Money other) {
    return amount >= other.amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money price = (Money) o;
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