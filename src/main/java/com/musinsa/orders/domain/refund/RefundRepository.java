package com.musinsa.orders.domain.refund;

import java.util.List;
import java.util.Optional;

public interface RefundRepository {

  Refund save(Refund refund);

  Optional<Refund> findById(Long refundId);

  List<Refund> findByOrderId(Long orderId);

}
