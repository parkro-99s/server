package com.parkro.server.domain.receipt.service;

import com.parkro.server.domain.receipt.dto.PostReceiptReq;
import com.parkro.server.domain.receipt.mapper.ReceiptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReceiptServiceImpl implements ReceiptService {

  private final ReceiptMapper receiptMapper;

  @Override

  public int addReceipt(PostReceiptReq req) {
    receiptMapper.insertReceipt(req);
    log.info("[receipt service] addReceipt: " + req.getReceiptId());
    return req.getReceiptId();
  }
}
