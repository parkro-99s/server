package com.parkro.server.domain.receipt.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostReceiptReq {

  @Setter
  private Integer receiptId;
  private Integer storeId;
  private Integer totalPrice;
}
