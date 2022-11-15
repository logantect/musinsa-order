package com.musinsa.orders.infra.refund;

import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class InMemoryRefundRepository implements RefundRepository {

  private final Map<Long, Refund> store = new HashMap<>();

  @Override
  public Refund save(Refund refund) {
    store.put(generateId(refund.id()), refund);
    return refund;
  }

  @Override
  public Optional<Refund> findById(Long refundId) {
    return Optional.ofNullable(store.get(refundId));
  }

  @Override
  public List<Refund> findByOrderId(final Long orderId) {
    return store.values().stream()
        .filter(refund -> orderId.equals(refund.orderId()))
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
