package com.musinsa.orders.domain.order;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShippingFeePolicyTest {

  private ShippingFeePolicy shippingFeePolicy;

  @BeforeEach
  void setUp() {
    shippingFeePolicy = new ShippingFeePolicy();
  }

  @DisplayName("5만원 미만 주문금액에 대해 유료 배송비 2500원을 반환한다")
  @Test
  void calculateShippingFee() {
    Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    Money actual = shippingFeePolicy.calculateShippingFee(order);
    assertThat(actual).isEqualTo(Money.from(2_500L));
  }

  @DisplayName("5만원 이상 주문금액에 대해 배송비 0원(무료배송금액)을 반환한다")
  @Test
  void calculateShippingFee_Free() {
    Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, "셔츠A", 40_000L),
        createOrderLineItem(2L, "셔츠B", 50_000L),
        createOrderLineItem(3L, "셔츠C", 60_000L)
    ));

    Money actual = shippingFeePolicy.calculateShippingFee(order);
    assertThat(actual).isEqualTo(Money.ZERO);
  }
}