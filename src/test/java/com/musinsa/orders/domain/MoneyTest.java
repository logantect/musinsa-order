package com.musinsa.orders.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.musinsa.orders.domain.order.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MoneyTest {

  @ParameterizedTest
  @DisplayName("가격을 생성할 수 있다.")
  @ValueSource(longs = {0, 19_000L})
  void createPrice(long value) {
    Money actual = Money.from(value);
    assertThat(actual).isEqualTo(Money.from(value));
  }

  @Test
  @DisplayName("가격은 0원 이상이어야 한다.")
  void createPrice_IllegalArgumentException() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> Money.from(-1));
  }

}