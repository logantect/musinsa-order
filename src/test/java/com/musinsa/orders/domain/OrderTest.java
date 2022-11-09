package com.musinsa.orders.domain;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName("주문생성")
  void placeOrder() {
    Order actual = createOrder(1L, List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    assertThat(actual).isNotNull();
    assertThat(actual.orderLineItems()).hasSize(3);
    assertThat(actual.orderLineItems()).extracting("productId", "name", "price")
        .contains(
            tuple(1L, "신발A", Price.from(15_000L)),
            tuple(2L, "신발B", Price.from(16_000L)),
            tuple(3L, "신발C", Price.from(17_000L))
        );
  }

}