package com.musinsa.orders.domain;

public interface ShippingFeePolicy {

  Money calculateShippingFee(Order order);
}
