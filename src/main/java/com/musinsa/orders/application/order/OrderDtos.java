package com.musinsa.orders.application.order;

import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

public class OrderDtos {

  @Data
  public static class OrderRequest {

    private List<OrderLineItemRequest> orderLineItems = List.of();

    public List<OrderLineItem> toOrderLineItems() {
      return orderLineItems.stream()
          .map(it -> new OrderLineItem(it.getProductId(), it.getName(), it.getPrice()))
          .collect(Collectors.toList());
    }
  }

  @Data
  public static class OrderLineItemRequest {

    private Long id;
    private Long productId;
    private String name;
    private Long price;
  }

  @Data
  public static class OrderResponse {

    private Long id;

    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(final Order order) {
      this.id = order.id();
      this.orderLineItems = order.orderLineItems().stream()
          .map(OrderLineItemResponse::new)
          .collect(Collectors.toList());
    }
  }

  @Data
  public static class OrderLineItemResponse {

    private Long id;
    private Long productId;
    private String name;
    private Long price;

    public OrderLineItemResponse(final OrderLineItem orderLineItem) {
      this.id = orderLineItem.id();
      this.productId = orderLineItem.productId();
      this.name = orderLineItem.name().toString();
      this.price = orderLineItem.price().amount();
    }
  }

}
