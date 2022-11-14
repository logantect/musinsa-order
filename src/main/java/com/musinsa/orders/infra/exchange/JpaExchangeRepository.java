package com.musinsa.orders.infra.exchange;

import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaExchangeRepository extends ExchangeRepository, JpaRepository<Exchange, Long> {

}
