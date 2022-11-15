package com.musinsa.orders.domain.refund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RefundLineItems {

  @ElementCollection
  @CollectionTable(
      name = "refund_line_item",
      joinColumns = @JoinColumn(
          name = "refund_id",
          nullable = false,
          foreignKey = @ForeignKey(name = "fk_refund_line_item_to_refund")
      )
  )
  private List<RefundLineItem> refundLineItems = new ArrayList<>();

  public RefundLineItems(List<RefundLineItem> refundLineItems) {
    if (refundLineItems == null || refundLineItems.isEmpty()) {
      throw new IllegalArgumentException();
    }
    this.refundLineItems = refundLineItems;
  }

  public static RefundLineItems from(List<Long> refundRequestLineItemIds) {
    return new RefundLineItems(refundRequestLineItemIds.stream()
        .map(RefundLineItem::new)
        .collect(Collectors.toList()));
  }

  public List<RefundLineItem> refundLineItems() {
    return Collections.unmodifiableList(refundLineItems);
  }

  public boolean existRefundLineItem(List<RefundLineItem> refundLineItems) {
    return existRefundLineItem(new RefundLineItems(refundLineItems));
  }

  public boolean existRefundLineItem(RefundLineItems refundLineItems) {
    return this.refundLineItems.stream().anyMatch(refundLineItems::contains);
  }

  public boolean contains(RefundLineItem refundLineItem) {
    return refundLineItems.contains(refundLineItem);
  }

  public RefundLineItems concat(List<RefundLineItem> refundLineItems) {
    return new RefundLineItems(
        Stream.concat(
            this.refundLineItems.stream(),
            refundLineItems.stream()
        ).toList()
    );
  }

  public List<Long> getOrderLineItemIds() {
    return refundLineItems.stream()
        .map(RefundLineItem::orderLineItemId)
        .collect(Collectors.toList());
  }

}