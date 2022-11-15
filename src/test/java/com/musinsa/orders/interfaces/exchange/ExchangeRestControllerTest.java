package com.musinsa.orders.interfaces.exchange;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static com.musinsa.orders.Fixtures.exchange;
import static com.musinsa.orders.Fixtures.exchangeLineItem;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import com.musinsa.orders.application.exchange.ExchangeDtos.ExchangeLineItemsRequest;
import com.musinsa.orders.application.exchange.ExchangeDtos.ExchangeRequest;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import com.musinsa.orders.domain.order.OrderRepository;
import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.ExchangeReason;
import com.musinsa.orders.domain.exchange.ExchangeReason.ExchangeReasonType;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
@DisplayName("교환 API")
class ExchangeRestControllerTest {

  @Autowired
  private ExchangeRepository exchangeRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Value("${local.server.port}")
  int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
    exchangeRepository.deleteAll();
  }

  @Nested
  @DisplayName("교환 등록 요청에")
  class CreateExchange {

    private final Order order = createOrder(List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    @Test
    @DisplayName("응답으로 201 Created와 생성된 교환정보를 반환한다")
    void createExchange() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);

      ExchangeRequest request = new ExchangeRequest(
          savedOrder.id(),
          new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
          5_000L,
          List.of(
              new ExchangeLineItemsRequest(orderLineItemA.id()),
              new ExchangeLineItemsRequest(orderLineItemB.id())
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/exchanges").
          then().
          statusCode(HttpStatus.CREATED.value()).
          log().all()
          .body("exchangeLineItems", hasSize(2));
    }
  }

  @Nested
  @DisplayName("교환 조회 요청에")
  class GetExchange {

    private final Order order = createOrder(List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    @Test
    @DisplayName("응답으로 200 OK와 교환정보를 반환한다")
    void getExchange() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);

      Exchange savedExchange = exchangeRepository.save(
          exchange(
              savedOrder.id(),
              new ExchangeReason(ExchangeReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(exchangeLineItem(orderLineItemA.id()), exchangeLineItem(orderLineItemB.id()))
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          log().all().
          when().
          get("/api/v1/exchanges/{exchangeId}", savedExchange.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("exchangeLineItems", hasSize(2));
    }
  }
}