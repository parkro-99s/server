package com.parkro.server.domain.payment.mapper;

import com.parkro.server.domain.payment.dto.PostPaymentReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

  void insertPayment(PostPaymentReq req);
}
