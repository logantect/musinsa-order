package com.musinsa.orders.interfaces.order;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static com.musinsa.orders.Fixtures.exchange;
import static com.musinsa.orders.Fixtures.exchangeLineItem;
import static com.musinsa.orders.Fixtures.refund;
import static com.musinsa.orders.Fixtures.refundLineItem;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.musinsa.orders.application.order.OrderDtos.OrderLineItemRequest;
import com.musinsa.orders.application.order.OrderDtos.OrderRequest;
import com.musinsa.orders.application.order.OrderDtos.RefundLineItemRequest;
import com.musinsa.orders.domain.exchange.ExchangeReason;
import com.musinsa.orders.domain.exchange.ExchangeReason.ExchangeReasonType;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import com.musinsa.orders.domain.order.OrderRepository;
import com.musinsa.orders.domain.refund.RefundReason;
import com.musinsa.orders.domain.refund.RefundReason.RefundReasonType;
import com.musinsa.orders.domain.refund.RefundRepository;
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

  @Autowired
  private RefundRepository refundRepository;

  @Autowired
  private ExchangeRepository exchangeRepository;

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

  @Nested
  @DisplayName("주문 번호 1번")
  class ProblemOrder1 {

    private final Order order = createOrder(List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    @Test
    @DisplayName("신발A 상품 교환에 반품비 5,000원을 반환한다")
    void case1() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      RefundLineItemRequest request = new RefundLineItemRequest(List.of(orderLineItemA.id()));

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/orders/{orderId}/exchanges/calculate", savedOrder.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("returnShippingFee", equalTo(5_000));
    }

    @Test
    @DisplayName("신발A 상품 교환 다음 신발B 환불에 반품비 2,500원을 반환한다")
    void case2() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);
      RefundLineItemRequest request = new RefundLineItemRequest(List.of(orderLineItemB.id()));

      exchangeRepository.save(
          exchange(
              1L,
              savedOrder.id(),
              new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(exchangeLineItem(orderLineItemA.id()))
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/orders/{orderId}/refunds/calculate", savedOrder.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("returnShippingFee", equalTo(2_500));
    }

    @Test
    @DisplayName("신발B 상품 환불 다음 신발C와 신발A를 한번에 환불에 반품비 2,500원을 반환한다")
    void case3() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);
      OrderLineItem orderLineItemC = savedOrder.getOrderLineItems().get(2);
      RefundLineItemRequest request = new RefundLineItemRequest(
          List.of(orderLineItemA.id(), orderLineItemC.id()));

      exchangeRepository.save(
          exchange(
              1L,
              savedOrder.id(),
              new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(exchangeLineItem(orderLineItemA.id()))
          )
      );

      refundRepository.save(
          refund(
              1L,
              savedOrder.id(),
              new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(2_500L),
              List.of(refundLineItem(orderLineItemB.id()))
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/orders/{orderId}/refunds/calculate", savedOrder.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("returnShippingFee", equalTo(2_500));
    }

  }

  @Nested
  @DisplayName("주문 번호 2번")
  class ProblemOrder2 {

    private final Order order = createOrder(List.of(
        createOrderLineItem(1L, "셔츠A", 40_000L),
        createOrderLineItem(2L, "셔츠B", 50_000L),
        createOrderLineItem(3L, "셔츠C", 60_000L)
    ));

    @Test
    @DisplayName("셔츠A 상품 환불에 반품비 2,500원을 반환한다")
    void case1() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      RefundLineItemRequest request = new RefundLineItemRequest(List.of(orderLineItemA.id()));

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/orders/{orderId}/refunds/calculate", savedOrder.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("returnShippingFee", equalTo(2_500));
    }

    @Test
    @DisplayName("셔츠A 상품 환불 다음 셔츠B와 C를 한번에 환불에 반품비 5,000원을 반환한다")
    void case2() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);
      OrderLineItem orderLineItemC = savedOrder.getOrderLineItems().get(2);
      RefundLineItemRequest request = new RefundLineItemRequest(
          List.of(orderLineItemB.id(), orderLineItemC.id()));

      refundRepository.save(
          refund(
              1L,
              savedOrder.id(),
              new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(2_500L),
              List.of(refundLineItem(orderLineItemA.id()))
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/orders/{orderId}/refunds/calculate", savedOrder.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("returnShippingFee", equalTo(5_000));
    }

  }


}