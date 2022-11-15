package com.musinsa.orders.infra.exchange;

import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class InMemoryExchangeRepository implements ExchangeRepository {

  private final Map<Long, Exchange> store = new HashMap<>();

  @Override
  public Exchange save(Exchange exchange) {
    store.put(generateId(exchange.id()), exchange);
    return exchange;
  }

  @Override
  public Optional<Exchange> findById(Long exchangeId) {
    return Optional.ofNullable(store.get(exchangeId));
  }

  @Override
  public List<Exchange> findByOrderId(final Long orderId) {
    return store.values().stream()
        .filter(exchange -> orderId.equals(exchange.orderId()))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteAll() {
    store.clear();
  }

  private Long generateId(final Long id) {
    return Optional.ofNullable(id).orElse(new Random().nextLong());
  }
}
