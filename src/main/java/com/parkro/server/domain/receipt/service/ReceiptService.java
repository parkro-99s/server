package com.parkro.server.domain.receipt.service;

import com.parkro.server.domain.receipt.dto.PostReceiptReq;

public interface ReceiptService {

  int addReceipt(PostReceiptReq req);
}