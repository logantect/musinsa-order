package com.musinsa.orders.application.exchange;

import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.Exchange.ExchangeStatus;
import com.musinsa.orders.domain.exchange.ExchangeLineItem;
import com.musinsa.orders.domain.exchange.ExchangeReason;
import com.musinsa.orders.domain.exchange.ExchangeReason.ExchangeReasonType;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeDtos {

  public record ExchangeRequest(
      Long orderId,
      ExchangeReason reason,
      Long returnShippingFee,
      List<ExchangeLineItemsRequest> exchangeLineItems
  ) {

    public Exchange toEntity() {
      return new Exchange(
          orderId,
          new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
          Money.from(returnShippingFee),
          exchangeLineItems.stream()
              .map(it -> new ExchangeLineItem(it.orderLineItemId))
              .collect(Collectors.toList())
      );
    }
  }

  public record ExchangeLineItemsRequest(
      Long orderLineItemId
  ) {

  }

  public record ExchangeResponse(
      Long id,
      Long orderId,
      ExchangeStatus status,
      ExchangeReason reason,
      Long returnShippingFee,
      List<ExchangeLineItemResponse> exchangeLineItems
  ) {

    public ExchangeResponse(Exchange exchange) {
      this(
          exchange.id(),
          exchange.orderId(),
          exchange.status(),
          exchange.reason(),
          exchange.returnShippingFee().amount(),
          exchange.getExchangeLineItems().stream()
              .map(it -> new ExchangeLineItemResponse(it.orderLineItemId()))
              .collect(Collectors.toList())
      );
    }
  }

  public record ExchangeLineItemResponse(
      Long orderLineItemId
  ) {

  }
}
