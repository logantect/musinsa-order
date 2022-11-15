package com.musinsa.orders.application.exchange;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.musinsa.orders.application.exchange.ExchangeDtos.ExchangeRequest;
import com.musinsa.orders.application.exchange.ExchangeDtos.ExchangeResponse;
import com.musinsa.orders.domain.exchange.Exchange;
import com.musinsa.orders.domain.exchange.ExchangeLineItem;
import com.musinsa.orders.domain.exchange.ExchangeLineItems;
import com.musinsa.orders.domain.exchange.ExchangeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExchangeService {

  private final ExchangeRepository exchangeRepository;
  
  @Transactional
  public ExchangeResponse createExchange(ExchangeRequest dto) {
    Exchange requestExchange = dto.toEntity();
    validateExist(requestExchange);
    return new ExchangeResponse(exchangeRepository.save(requestExchange));
  }

  public ExchangeResponse getExchange(final Long exchangeId) {
    return new ExchangeResponse(
        exchangeRepository.findById(exchangeId).orElseThrow(IllegalArgumentException::new)
    );
  }

  private void validateExist(Exchange requestExchange) {
    List<Exchange> exchanges = exchangeRepository.findByOrderId(requestExchange.orderId());
    List<ExchangeLineItem> exchangedLineItems = exchanges.stream()
        .flatMap(it -> it.getExchangeLineItems().stream())
        .collect(Collectors.toList());

    ExchangeLineItems exchangeLineItems = requestExchange.exchangeLineItems();
    if (!isEmpty(exchangedLineItems) && exchangeLineItems.existExchangeLineItem(exchangedLineItems)) {
      throw new IllegalArgumentException("이미 교환진행 중이거나 교환완료된 주문 상품이 존재합니다");
    }
  }

}
