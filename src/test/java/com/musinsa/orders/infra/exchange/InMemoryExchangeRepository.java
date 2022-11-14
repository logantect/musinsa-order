package com.musinsa.orders.infra.exchange;

import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryExchangeRepository implements ExchangeRepository {

  private final Map<Long, Exchange> store = new HashMap<>();

  @Override
  public Exchange save(Exchange exchange) {
    store.put(exchange.id(), exchange);
    return exchange;
  }

  @Override
  public List<Exchange> findByOrderId(final Long orderId) {
    return store.values().stream()
        .filter(exchange -> orderId.equals(exchange.orderId()))
        .collect(Collectors.toList());
  }
}
