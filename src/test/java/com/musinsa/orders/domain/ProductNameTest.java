package com.musinsa.orders.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ProductNameTest {

  @DisplayName("상품명을 생성할 수 있다")
  @Test
  void create() {
    ProductName actual = ProductName.from("상품A");
    assertThat(actual).isEqualTo(ProductName.from("상품A"));
  }

  @DisplayName("상품명이 올바르지 않으면 등록할 수 없다.")
  @NullAndEmptySource
  @ParameterizedTest
  void create_NotValidNumber(final String name) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> ProductName.from(name));
  }

}