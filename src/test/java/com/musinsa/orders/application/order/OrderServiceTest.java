package com.musinsa.orders.application.order;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static com.musinsa.orders.Fixtures.exchange;
import static com.musinsa.orders.Fixtures.exchangeLineItem;
import static com.musinsa.orders.Fixtures.refund;
import static com.musinsa.orders.Fixtures.refundLineItem;

import com.musinsa.orders.domain.exchange.ExchangePolicy;
import com.musinsa.orders.domain.exchange.ExchangeReason;
import com.musinsa.orders.domain.exchange.ExchangeReason.ExchangeReasonType;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderRepository;
import com.musinsa.orders.domain.order.ShippingFeePolicy;
import com.musinsa.orders.domain.refund.RefundPolicy;
import com.musinsa.orders.domain.refund.RefundReason;
import com.musinsa.orders.domain.refund.RefundReason.RefundReasonType;
import com.musinsa.orders.domain.refund.RefundRepository;
import com.musinsa.orders.infra.exchange.InMemoryExchangeRepository;
import com.musinsa.orders.infra.order.InMemoryOrderRepository;
import com.musinsa.orders.infra.refund.InMemoryRefundRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 서비스")
class OrderServiceTest {

  private OrderService orderService;
  private OrderRepository orderRepository;
  private RefundRepository refundRepository;
  private RefundPolicy refundPolicy;
  private ExchangeRepository exchangeRepository;
  private ExchangePolicy exchangePolicy;

  @BeforeEach
  void setUp() {
    orderRepository = new InMemoryOrderRepository();
    refundRepository = new InMemoryRefundRepository();
    refundPolicy = new RefundPolicy(refundRepository);
    exchangeRepository = new InMemoryExchangeRepository();
    exchangePolicy = new ExchangePolicy();
    orderService = new OrderService(orderRepository, refundPolicy, exchangePolicy,
        new ShippingFeePolicy());
  }

  @Nested
  @DisplayName("주문 번호 1번")
  class ProblemOrder1 {

    private final Order order = createOrder(1L, List.of(
        createOrderLineItem(1L, 1L, "신발A", 15_000L),
        createOrderLineItem(2L, 2L, "신발B", 16_000L),
        createOrderLineItem(3L, 3L, "신발C", 17_000L)
    ));

    @Test
    @DisplayName("신발A 상품 교환에 반품비 5,000원을 반환한다")
    void case1() {
      Order savedOrder = orderRepository.save(order);
      Money actual = orderService.calculateExchangeShippingFee(savedOrder.id(), List.of(1L));
      Assertions.assertEquals(actual, Money.from(5_000L));
    }

    @Test
    @DisplayName("신발A 상품 교환 다음 신발B 환불에 반품비 2,500원을 반환한다")
    void case2() {
      Order savedOrder = orderRepository.save(order);
      exchangeRepository.save(
          exchange(
              1L,
              savedOrder.id(),
              new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(exchangeLineItem(1L))
          )
      );

      Money actual = orderService.calculateRefundShippingFee(savedOrder.id(), List.of(1L));
      Assertions.assertEquals(actual, Money.from(2_500L));
    }

    @Test
    @DisplayName("신발B 상품 환불 다음 신발C와 신발A를 한번에 환불에 반품비 5,000원을 반환한다")
    void case3() {
      Order savedOrder = orderRepository.save(order);
      exchangeRepository.save(
          exchange(
              1L,
              savedOrder.id(),
              new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(exchangeLineItem(1L))
          )
      );

      refundRepository.save(
          refund(
              1L,
              savedOrder.id(),
              new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(2_500L),
              List.of(refundLineItem(2L))
          )
      );

      Money actual = orderService.calculateRefundShippingFee(savedOrder.id(), List.of(3L, 1L));
      Assertions.assertEquals(actual, Money.from(2_500L));
    }

  }

}