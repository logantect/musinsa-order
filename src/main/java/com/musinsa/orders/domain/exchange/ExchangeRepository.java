package com.musinsa.orders.domain.exchange;

import java.util.List;

public interface ExchangeRepository {

  Exchange save(Exchange exchange);

  List<Exchange> findByOrderId(Long orderId);
}
