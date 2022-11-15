package com.musinsa.orders.interfaces.refund;

import com.musinsa.orders.application.refund.RefundDtos.RefundRequest;
import com.musinsa.orders.application.refund.RefundDtos.RefundResponse;
import com.musinsa.orders.application.refund.RefundService;
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
@RequestMapping(path = "/api/v1/refunds")
public class RefundRestController {

  private final RefundService refundService;

  @PostMapping
  public ResponseEntity<ApiResponse<RefundResponse>> createRefund(
      @RequestBody @Valid RefundRequest request
  ) {
    RefundResponse refundResponse = refundService.createRefund(request);
    return ResponseEntity.created(URI.create("/api/v1/refunds/" + refundResponse.id()))
        .body(new ApiResponse<>(refundResponse));
  }

  @GetMapping("/{refundId}")
  public ResponseEntity<ApiResponse<RefundResponse>> getRefund(
      @PathVariable final Long refundId
  ) {
    RefundResponse refundResponse = refundService.getRefund(refundId);
    return ResponseEntity.ok(new ApiResponse<>(refundResponse));
  }

}
