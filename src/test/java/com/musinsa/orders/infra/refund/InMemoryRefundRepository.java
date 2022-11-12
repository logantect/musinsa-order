package com.musinsa.orders.infra.refund;

import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryRefundRepository implements RefundRepository {

  private final Map<Long, Refund> store = new HashMap<>();

  @Override
  public Refund save(Refund refund) {
    store.put(refund.id(), refund);
    return refund;
  }

  @Override
  public List<Refund> findByOrderId(final Long orderId) {
    return store.values().stream()
        .filter(refund -> orderId.equals(refund.orderId()))
        .collect(Collectors.toList());
  }
}
