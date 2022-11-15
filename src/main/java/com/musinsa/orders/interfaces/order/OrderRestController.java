package com.musinsa.orders.interfaces.order;

import com.musinsa.orders.application.order.OrderDtos;
import com.musinsa.orders.application.order.OrderDtos.OrderResponse;
import com.musinsa.orders.application.order.OrderService;
import com.musinsa.orders.interfaces.support.ApiResponse;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/orders")
public class OrderRestController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
      @RequestBody @Valid OrderDtos.OrderRequest request
  ) {
    OrderResponse orderResponse = orderService.placeOrder(request);
    return ResponseEntity.created(URI.create("/api/v1/orders/" + orderResponse.getId()))
        .body(new ApiResponse<>(orderResponse));
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
      @PathVariable final Long orderId
  ) {
    OrderResponse orderResponse = orderService.getOrder(orderId);
    return ResponseEntity.ok(new ApiResponse<>(orderResponse));
  }
}
