package com.musinsa.orders.interfaces.exchange;

import com.musinsa.orders.application.exchange.ExchangeDtos.ExchangeRequest;
import com.musinsa.orders.application.exchange.ExchangeDtos.ExchangeResponse;
import com.musinsa.orders.application.exchange.ExchangeService;
import com.musinsa.orders.interfaces.support.ApiResponse;
import java.net.URI;
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
@RequestMapping(path = "/api/v1/exchanges")
public class ExchangeRestController {

  private final ExchangeService exchangeService;

  @PostMapping
  public ResponseEntity<ApiResponse<ExchangeResponse>> createExchange(
      @RequestBody @Valid ExchangeRequest request
  ) {
    ExchangeResponse exchangeResponse = exchangeService.createExchange(request);
    return ResponseEntity.created(URI.create("/api/v1/exchanges/" + exchangeResponse.id()))
        .body(new ApiResponse<>(exchangeResponse));
  }

  @GetMapping("/{exchangeId}")
  public ResponseEntity<ApiResponse<ExchangeResponse>> getExchange(
      @PathVariable final Long exchangeId
  ) {
    ExchangeResponse exchangeResponse = exchangeService.getExchange(exchangeId);
    return ResponseEntity.ok(new ApiResponse<>(exchangeResponse));
  }

}
