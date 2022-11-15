package com.musinsa.orders.domain.refund;

import static com.musinsa.orders.Fixtures.refund;
import static com.musinsa.orders.Fixtures.refundLineItem;
import static org.assertj.core.api.Assertions.assertThat;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.refund.Refund.RefundStatus;
import com.musinsa.orders.domain.refund.RefundReason.RefundReasonType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefundTest {

  @Test
  @DisplayName("1개 이상의 주문 상품으로 환불을 생성할 수 있다")
  void createRefund() {
    Refund actual = refund(
        1L,
        1L,
        new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
        Money.from(5_000L),
        List.of(refundLineItem(1L), refundLineItem(2L))
    );

    assertThat(actual).isNotNull();
    assertThat(actual.id()).isNotNull();
    assertThat(actual.orderId()).isEqualTo(1L);
    assertThat(actual.status()).isEqualTo(RefundStatus.ACCEPTED);
    assertThat(actual.reason()).isEqualTo(
        new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"));
    assertThat(actual.returnShippingFee()).isEqualTo(Money.from(5_000L));
    assertThat(actual.getRefundLineItems()).hasSize(2);
    assertThat(actual.getRefundLineItems()).extracting("orderLineItemId")
        .contains(1L, 2L);
  }

}