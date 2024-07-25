package com.parkro.server.domain.receipt.mapper;

import com.parkro.server.domain.receipt.dto.PostReceiptReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReceiptMapper {

  int insertReceipt(PostReceiptReq req);
}
