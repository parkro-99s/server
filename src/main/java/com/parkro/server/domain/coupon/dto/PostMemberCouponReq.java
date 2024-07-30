package com.parkro.server.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PostMemberCouponReq {
    private Long memberCouponId;
    private Long couponId;
    private Integer memberId;
    private String status;
    private Date createdDate;
    private Date endDate;
}
