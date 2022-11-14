package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static com.musinsa.orders.Fixtures.refund;
import static com.musinsa.orders.Fixtures.refundLineItem;
import static org.assertj.core.api.Assertions.assertThat;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.refund.RefundReason.RefundReasonType;
import com.musinsa.orders.infra.refund.InMemoryRefundRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RefundPolicyTest {

  private RefundPolicy refundPolicy;
  private RefundRepository refundRepository;

  @BeforeEach
  void setUp() {
    refundRepository = new InMemoryRefundRepository();
    refundPolicy = new RefundPolicy(refundRepository);
  }

  @Nested
  @DisplayName("유료 배송비 정책 주문")
  class ShippingFee {

    @Test
    @DisplayName("부분 환불시 반품비 2500원을 반환한다")
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
    @DisplayName("전체 환불시 반품비 2500원을 반환한다")
    void calculateReturnShippingFee_FullRefund() {
      Order order = createOrder(1L, List.of(
          createOrderLineItem(1L, 1L, "신발A", 15_000L),
          createOrderLineItem(2L, 2L, "신발B", 16_000L),
          createOrderLineItem(3L, 3L, "신발C", 17_000L)
      ));

      Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(1L, 2L, 3L));
      assertThat(actual).isEqualTo(Money.from(2_500L));
    }

    @Test
    @DisplayName("부분 환불 후 전체 환불시 반품비 2500원을 반환한다")
    void calculateReturnShippingFee_twiceFullReturn() {
      Order order = createOrder(1L, List.of(
          createOrderLineItem(1L, 1L, "신발A", 15_000L),
          createOrderLineItem(2L, 2L, "신발B", 16_000L),
          createOrderLineItem(3L, 3L, "신발C", 17_000L)
      ));

      refundRepository.save(
          refund(
              1L,
              1L,
              new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(2_500L),
              List.of(refundLineItem(1L))
          )
      );

      Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(2L, 3L));
      assertThat(actual).isEqualTo(Money.from(2_500L));
    }
  }

  @Nested
  @DisplayName("무료 배송비 정책 주문")
  class FreeShippingFee {

    @Test
    @DisplayName("부분 환불시 반품비 2500원을 반환한다")
    void calculateReturnShippingFee_FreeShippingFeePartialRefund() {
      Order order = createOrder(1L, List.of(
          createOrderLineItem(1L, 1L, "셔츠A", 40_000L),
          createOrderLineItem(2L, 2L, "셔츠B", 50_000L),
          createOrderLineItem(3L, 3L, "셔츠C", 60_000L)
      ));

      Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(2L));
      assertThat(actual).isEqualTo(Money.from(2_500L));
    }

    @Test
    @DisplayName("전체 환불시 반품비 5000원을 반환한다")
    void calculateReturnShippingFee_FreeShippingFeeFullRefund() {
      Order order = createOrder(1L, List.of(
          createOrderLineItem(1L, 1L, "셔츠A", 40_000L),
          createOrderLineItem(2L, 2L, "셔츠B", 50_000L),
          createOrderLineItem(3L, 3L, "셔츠C", 60_000L)
      ));

      Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(1L, 2L, 3L));
      assertThat(actual).isEqualTo(Money.from(5_000L));
    }

    @Test
    @DisplayName("부분 환불 후 전체 환불시 반품비 5000원을 반환한다")
    void calculateReturnShippingFee_twiceFullReturn() {
      Order order = createOrder(1L, List.of(
          createOrderLineItem(1L, 1L, "셔츠A", 40_000L),
          createOrderLineItem(2L, 2L, "셔츠B", 50_000L),
          createOrderLineItem(3L, 3L, "셔츠C", 60_000L)
      ));

      refundRepository.save(
          refund(
              1L,
              1L,
              new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(refundLineItem(1L))
          )
      );

      Money actual = refundPolicy.calculateReturnShippingFee(order, List.of(2L, 3L));
      assertThat(actual).isEqualTo(Money.from(5_000L));
    }
  }

}