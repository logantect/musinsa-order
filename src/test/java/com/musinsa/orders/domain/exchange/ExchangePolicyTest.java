package com.musinsa.orders.domain.exchange;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.musinsa.orders.domain.exchange.ExchangePolicy;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExchangePolicyTest {

  private ExchangePolicy exchangePolicy;

  @BeforeEach
  void setUp() {
    exchangePolicy = new ExchangePolicy();
  }

  @Test
  @DisplayName("주문 상품 교환 반품비 계산 요청에 왕복 반품비 5000원을 반환한다")
  void calculateReturnShippingFee() {
    Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, 1L, "신발A", 15_000L),
        createOrderLineItem(2L, 2L, "신발B", 16_000L),
        createOrderLineItem(3L, 3L, "신발C", 17_000L)
    ));

    Money actual = exchangePolicy.calculateReturnShippingFee(order, List.of(1L));
    assertThat(actual).isEqualTo(Money.from(5_000L));
  }

  @Test
  @DisplayName("주문 상품이 존재하지 않으면 반품비를 계산할 수 없다")
  void calculateReturnShippingFee_NotExistLineItem() {
    Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, 1L, "신발A", 15_000L),
        createOrderLineItem(2L, 2L, "신발B", 16_000L),
        createOrderLineItem(3L, 3L, "신발C", 17_000L)
    ));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> exchangePolicy.calculateReturnShippingFee(order, List.of(4L)));
  }

}