package com.parkro.server.domain.payment.service;

import com.parkro.server.domain.payment.dto.PostPaymentReq;

public interface PaymentService {

  Integer addPayment(PostPaymentReq req);
}