package com.parkro.server.domain.coupon.mapper;

import com.parkro.server.domain.coupon.dto.PostMemberCouponReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface CouponMapper {
    Integer selectCouponIdByDate(Date date);
    void insertCoupons(PostMemberCouponReq postMemberCouponReq);
}
