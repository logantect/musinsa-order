package com.musinsa.orders.domain.refund;

import java.util.List;

public interface RefundRepository {

  Refund save(Refund refund);

  List<Refund> findByOrderId(Long orderId);
}
