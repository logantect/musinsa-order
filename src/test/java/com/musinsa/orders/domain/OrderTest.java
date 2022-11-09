package com.musinsa.orders.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("주문생성")
  void createOrder() {
    Order actual = new Order(
        1L,
        List.of(
            new OrderLineItem(new Random().nextLong(), 1L, "신발A", 15_000L),
            new OrderLineItem(new Random().nextLong(), 2L, "신발B", 16_000L),
            new OrderLineItem(new Random().nextLong(), 3L, "신발C", 17_000L)
        )
    );

    assertThat(actual).isNotNull();
    assertThat(actual.orderLineItems()).hasSize(3);
    assertThat(actual.orderLineItems()).extracting("productId", "name", "price")
        .contains(
            tuple(1L, "신발A", 15_000L),
            tuple(2L, "신발B", 16_000L),
            tuple(3L, "신발C", 17_000L)
        );
  }
}