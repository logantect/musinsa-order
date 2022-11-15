package com.musinsa.orders.interfaces.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiResponse<T>(@JsonUnwrapped T body) {

}
