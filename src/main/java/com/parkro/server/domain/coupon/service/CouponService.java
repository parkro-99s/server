package com.parkro.server.domain.coupon.service;

import com.parkro.server.domain.coupon.dto.PostMemberCouponReq;

import java.util.Date;

public interface CouponService {

    Integer findCouponIdByDate(Date date);
    void addCoupons(PostMemberCouponReq postMemberCouponReq);
    void removeCoupons(Integer memberId);
}
