package com.musinsa.orders.interfaces.refund;

import static com.musinsa.orders.Fixtures.createOrder;
import static com.musinsa.orders.Fixtures.createOrderLineItem;
import static com.musinsa.orders.Fixtures.refund;
import static com.musinsa.orders.Fixtures.refundLineItem;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import com.musinsa.orders.application.refund.RefundDtos.RefundLineItemsRequest;
import com.musinsa.orders.application.refund.RefundDtos.RefundRequest;
import com.musinsa.orders.domain.order.Money;
import com.musinsa.orders.domain.order.Order;
import com.musinsa.orders.domain.order.OrderLineItem;
import com.musinsa.orders.domain.order.OrderRepository;
import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundReason;
import com.musinsa.orders.domain.refund.RefundReason.RefundReasonType;
import com.musinsa.orders.domain.refund.RefundRepository;
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
@DisplayName("환불 API")
class RefundRestControllerTest {

  @Autowired
  private RefundRepository refundRepository;

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
    refundRepository.deleteAll();
    orderRepository.deleteAll();
  }

  @Nested
  @DisplayName("환불 등록 요청에")
  class CreateRefund {

    private final Order order = createOrder(List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    @Test
    @DisplayName("응답으로 201 Created와 생성된 환불정보를 반환한다")
    void createRefund() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);

      RefundRequest request = new RefundRequest(
          savedOrder.id(),
          new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
          5_000L,
          List.of(
              new RefundLineItemsRequest(orderLineItemA.id()),
              new RefundLineItemsRequest(orderLineItemB.id())
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          body(request).
          log().all().
          when().
          post("/api/v1/refunds").
          then().
          statusCode(HttpStatus.CREATED.value()).
          log().all()
          .body("refundLineItems", hasSize(2));
    }
  }

  @Nested
  @DisplayName("환불 조회 요청에")
  class GetRefund {

    private final Order order = createOrder(List.of(
        createOrderLineItem(1L, "신발A", 15_000L),
        createOrderLineItem(2L, "신발B", 16_000L),
        createOrderLineItem(3L, "신발C", 17_000L)
    ));

    @Test
    @DisplayName("응답으로 200 OK와 환불정보를 반환한다")
    void getRefund() {
      Order savedOrder = orderRepository.save(order);
      OrderLineItem orderLineItemA = savedOrder.getOrderLineItems().get(0);
      OrderLineItem orderLineItemB = savedOrder.getOrderLineItems().get(1);

      Refund savedRefund = refundRepository.save(
          refund(
              savedOrder.id(),
              new RefundReason(RefundReasonType.CHANGE_OF_MIND, "상품 색상이 마음에 안들어요"),
              Money.from(5_000L),
              List.of(refundLineItem(orderLineItemA.id()), refundLineItem(orderLineItemB.id()))
          )
      );

      given().
          header("Content-type", MediaType.APPLICATION_JSON_VALUE).
          log().all().
          when().
          get("/api/v1/refunds/{refundId}", savedRefund.id()).
          then().
          statusCode(HttpStatus.OK.value()).
          log().all()
          .body("refundLineItems", hasSize(2));
    }
  }
}