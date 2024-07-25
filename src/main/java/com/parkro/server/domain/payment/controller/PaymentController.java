package com.parkro.server.domain.payment.controller;

import com.parkro.server.domain.payment.dto.PostPaymentReq;
import com.parkro.server.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 정산
 *
 * @author 김땡땡
 * @since 2024.07.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.07.25  김땡땡      최초 생성
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<Integer> paymentAdd(@RequestBody PostPaymentReq req) {
    return ResponseEntity.ok(paymentService.addPayment(req));
  }
}
