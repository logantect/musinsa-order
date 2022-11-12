package com.musinsa.orders.infra.refund;

import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRefundRepository extends RefundRepository, JpaRepository<Refund, Long> {
}
