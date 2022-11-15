package com.musinsa.orders.interfaces.order;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import com.musinsa.orders.application.order.OrderDtos.OrderLineItemRequest;
import com.musinsa.orders.application.order.OrderDtos.OrderRequest;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderRepository;
import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("주문 API")
class OrderRestControllerTest {

  @Autowired
  private OrderRepository orderRepository;

  @Value("${local.server.port}")
  int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Nested
  @DisplayName("주문 등록 요청에")
  class PlaceOrder {

    @Test
    @DisplayName("응답으로 201 Created와 생성된 주문을 반환한다")
    void placeOrder() {
      OrderRequest request = new OrderRequest(
          List.of(
              new OrderLineItemRequest(1L, "셔츠A", 40_000L),
              new OrderLineItemRequest(2L, "셔츠B", 50_000L),
              new OrderLineItemRequest(3L, "셔츠C", 60_000L)
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/orders").
          then().
          statusCode(HttpStatus.CREATED.value()).
          log().all()
          .body("orderLineItems", hasSize(3));
    }
  }

  @Nested
  @DisplayName("주문 조회 요청에")
  class GetOrder {

    @Test
    @DisplayName("응답으로 200 OK와 주문을 반환한다")
    void getOrder() {
      Order order = orderRepository.save(
          createOrder(1L, List.of(
              createOrderLineItem(1L, "신발A", 15_000L),
              createOrderLineItem(2L, "신발B", 16_000L),
              createOrderLineItem(3L, "신발C", 17_000L)
          ))
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          log().all().
          when().
          get("/api/v1/orders/{orderId}", order.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("orderLineItems", hasSize(3));
    }
  }

}