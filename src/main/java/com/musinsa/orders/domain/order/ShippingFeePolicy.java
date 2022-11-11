package com.musinsa.orders.domain.order;

public interface ShippingFeePolicy {

  Money calculateShippingFee(Order order);
}
