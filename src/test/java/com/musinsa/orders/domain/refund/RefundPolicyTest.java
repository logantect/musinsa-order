package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.refund.RefundPolicy;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefundPolicyTest {

  private RefundPolicy refundPolicy;

  @BeforeEach
  void setUp() {
    refundPolicy = new RefundPolicy();
  }

  @Test
  @DisplayName("유료 배송비 정책 주문 부분 환불시 반품비 2500원을 반환한다")
  void calculateReturnShippingFee() {
    Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, 1L, "신발A", 15_000L),
        createOrderLineItem(2L, 2L, "신발B", 16_000L),
        createOrderLineItem(3L, 3L, "신발C", 17_000L)
    ));

    Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(2L));
    assertThat(actual).isEqualTo(Money.from(2_500L));
  }

  @Test
  @DisplayName("유료 배송비 정책 주문 전체 환불시 반품비 2500원을 반환한다")
  void calculateReturnShippingFee_FullRefund() {
    Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, 1L, "신발A", 15_000L),
        createOrderLineItem(2L, 2L, "신발B", 16_000L),
        createOrderLineItem(3L, 3L, "신발C", 17_000L)
    ));

    Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(1L, 2L, 3L));
    assertThat(actual).isEqualTo(Money.from(2_500L));
  }
}