package com.musinsa.orders.domain.exchange;

import java.util.List;
import java.util.Optional;

public interface ExchangeRepository {

  Exchange save(Exchange exchange);

  List<Exchange> findByOrderId(Long orderId);

  Optional<Exchange> findById(Long exchangeId);
}
