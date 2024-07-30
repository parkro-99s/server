package com.parkro.server.domain.coupon.service;

import com.parkro.server.domain.coupon.dto.PostMemberCouponReq;
import com.parkro.server.domain.coupon.mapper.CouponMapper;
import com.parkro.server.exception.CustomException;
import com.parkro.server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponServiceImpl implements CouponService {
    private final CouponMapper couponMapper;
    @Override
    public Integer findCouponIdByDate(Date date) {

        int couponId = 0;
        couponId = couponMapper.selectCouponIdByDate(date);

        if(couponId == 0) {
            throw new CustomException(ErrorCode.INVALID_COUPON_ID);
        }
        return couponId;
    }

    @Override
    public void addCoupons(PostMemberCouponReq postMemberCouponReq) {
        PostMemberCouponReq modifiedMemberCouponReq = PostMemberCouponReq.builder()
                .memberId(postMemberCouponReq.getMemberId())
                .couponId(postMemberCouponReq.getCouponId())
                .build();

        couponMapper.insertCoupons(modifiedMemberCouponReq);
    }
}
