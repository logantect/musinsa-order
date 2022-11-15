package com.musinsa.orders.domain.exchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ExchangeLineItems {

  @ElementCollection
  @CollectionTable(name = "exchange_line_item", joinColumns = @JoinColumn(name = "exchange_id"))
  private List<ExchangeLineItem> exchangeLineItems = new ArrayList<>();

  public ExchangeLineItems(List<ExchangeLineItem> exchangeLineItems) {
    if (exchangeLineItems == null || exchangeLineItems.isEmpty()) {
      throw new IllegalArgumentException();
    }
    this.exchangeLineItems = exchangeLineItems;
  }

  public static ExchangeLineItems from(List<Long> exchangeRequestLineItemIds) {
    return new ExchangeLineItems(exchangeRequestLineItemIds.stream()
        .map(ExchangeLineItem::new)
        .collect(Collectors.toList()));
  }

  public List<ExchangeLineItem> exchangeLineItems() {
    return Collections.unmodifiableList(exchangeLineItems);
  }

  public boolean existExchangeLineItem(List<ExchangeLineItem> exchangeLineItems) {
    return existExchangeLineItem(new ExchangeLineItems(exchangeLineItems));
  }

  public boolean existExchangeLineItem(ExchangeLineItems exchangeLineItems) {
    return this.exchangeLineItems.stream().anyMatch(exchangeLineItems::contains);
  }

  public boolean contains(ExchangeLineItem exchangeLineItem) {
    return exchangeLineItems.contains(exchangeLineItem);
  }

}