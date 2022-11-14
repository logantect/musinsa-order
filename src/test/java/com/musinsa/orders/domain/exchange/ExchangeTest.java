package com.musinsa.orders.domain.exchange;

import static com.musinsa.orders.Fixtures.exchange;
import static com.musinsa.orders.Fixtures.exchangeLineItem;
import static org.assertj.core.api.Assertions.assertThat;

import com.musinsa.orders.domain.exchange.Exchange.ExchangeStatus;
import com.musinsa.orders.domain.exchange.ExchangeReason.ExchangeReasonType;
import com.musinsa.orders.domain.order.Money;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExchangeTest {

  @Test
  @DisplayName("1개 이상의 주문 상품으로 교환을 생성할 수 있다")
  void createExchange() {
    Exchange actual = exchange(
        1L,
        1L,
        new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
        Money.from(5_000L),
        List.of(exchangeLineItem(1L), exchangeLineItem(2L))
    );

    assertThat(actual).isNotNull();
    assertThat(actual.id()).isNotNull();
    assertThat(actual.orderId()).isEqualTo(1L);
    assertThat(actual.status()).isEqualTo(ExchangeStatus.ACCEPTED);
    assertThat(actual.reason()).isEqualTo(
        new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"));
    assertThat(actual.returnShippingFee()).isEqualTo(Money.from(5_000L));
    assertThat(actual.exchangeLineItems()).hasSize(2);
    assertThat(actual.exchangeLineItems()).extracting("orderLineItemId")
        .contains(1L, 2L);
  }

}