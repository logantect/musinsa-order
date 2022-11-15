package com.musinsa.orders.application.refund;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.musinsa.orders.application.refund.RefundDtos.RefundRequest;
import com.musinsa.orders.application.refund.RefundDtos.RefundResponse;
import com.musinsa.orders.domain.refund.Refund;
import com.musinsa.orders.domain.refund.RefundLineItem;
import com.musinsa.orders.domain.refund.RefundLineItems;
import com.musinsa.orders.domain.refund.RefundRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefundService {

  private final RefundRepository refundRepository;
  
  @Transactional
  public RefundResponse createRefund(RefundRequest dto) {
    Refund requestRefund = dto.toEntity();
    validateExist(requestRefund);
    return new RefundResponse(refundRepository.save(requestRefund));
  }

  public RefundResponse getRefund(final Long refundId) {
    return new RefundResponse(
        refundRepository.findById(refundId).orElseThrow(IllegalArgumentException::new)
    );
  }

  private void validateExist(Refund requestRefund) {
    List<Refund> refunds = refundRepository.findByOrderId(requestRefund.orderId());
    List<RefundLineItem> refundedLineItems = refunds.stream()
        .flatMap(it -> it.getRefundLineItems().stream())
        .collect(Collectors.toList());

    RefundLineItems refundLineItems = requestRefund.refundLineItems();
    if (!isEmpty(refundedLineItems) && refundLineItems.existRefundLineItem(refundedLineItems)) {
      throw new IllegalArgumentException("이미 환불진행 중이거나 환불완료된 주문 상품이 존재합니다");
    }
  }

}
