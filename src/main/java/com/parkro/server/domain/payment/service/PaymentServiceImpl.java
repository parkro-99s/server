package com.parkro.server.domain.payment.service;

import com.parkro.server.domain.payment.dto.PostPaymentReq;
import com.parkro.server.domain.payment.mapper.PaymentMapper;
import com.parkro.server.domain.receipt.dto.PostReceiptReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentServiceImpl implements PaymentService {

  private final PaymentMapper paymentMapper;

  @Override
  @Transactional
  public Integer addPayment(PostPaymentReq req) {
    paymentMapper.insertPayment(req);
    log.info("[payment service] addPayment: " + req.getPaymentId());
    return req.getPaymentId();
  }
}
