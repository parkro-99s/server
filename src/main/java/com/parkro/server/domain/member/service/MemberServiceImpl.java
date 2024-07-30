package com.parkro.server.domain.member.service;

import com.parkro.server.domain.coupon.dto.PostMemberCouponReq;
import com.parkro.server.domain.coupon.service.CouponService;
import com.parkro.server.domain.member.dto.GetMemberRes;
import com.parkro.server.domain.member.dto.PostMemberReq;
import com.parkro.server.domain.member.dto.PostSignInRes;
import com.parkro.server.domain.member.dto.PutMemberReq;
import com.parkro.server.domain.member.dto.PostMemberRes;
import com.parkro.server.domain.member.mapper.MemberMapper;
import com.parkro.server.domain.parking.mapper.ParkingMapper;
import com.parkro.server.exception.CustomException;
import com.parkro.server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import com.parkro.server.util.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ParkingMapper parkingMapper;
    private final CouponService couponService;

    @Transactional
    @Override
    public void findUsername(String username) {

        Optional<PostMemberReq> optionalPostMemberReq = memberMapper.selectMemberName(username);

        if (optionalPostMemberReq.isPresent()) {

            throw new CustomException(ErrorCode.FIND_DUPLICATED_USERNAME);


        }
    }

    @Override
    @Transactional
    public void addMember(PostMemberReq postMemberReq) {

        String hashedPassword = passwordEncoder.encode(postMemberReq.getPassword());

        postMemberReq.setPassword(hashedPassword);

        memberMapper.insertMember(postMemberReq);

        parkingMapper.updateMemberId(postMemberReq);

        GetMemberRes getMemberRes = findMember(postMemberReq.getUsername());
        long couponId = couponService.findCouponIdByDate(getMemberRes.getCreatedDate());
        PostMemberCouponReq modifiedReq = PostMemberCouponReq.builder()
                .memberId(getMemberRes.getMemberId())
                .couponId(couponId)
                .build();
        couponService.addCoupons(modifiedReq);
    }


    @Override
    @Transactional
    public PostMemberRes signInMember(PostMemberReq postMemberReq) {

        GetMemberRes member = memberMapper.selectMemberByUsername(postMemberReq.getUsername());

        if (member == null) {
            throw new CustomException(ErrorCode.FAIL_SIGN_IN);
        }

        if (!passwordEncoder.matches(postMemberReq.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.FAIL_SIGN_IN);
        }

        String token = jwtTokenProvider.createToken(member.getUsername(), Collections.singletonList(member.getROLE()));

        PostSignInRes postSignInRes = PostSignInRes.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .build();

        return new PostMemberRes(postSignInRes, token);
    }


    @Override
    public GetMemberRes findMember(String username) {

        GetMemberRes member = memberMapper.selectMemberByUsername(username);
      
        if (member == null) {
            throw new CustomException(ErrorCode.FIND_FAIL_USER_ID);
        }
        return member;
    }
  
    @Override
    public Integer removeMember(String username) {

        int cnt = memberMapper.deleteMember(username);

        if(cnt == 0){
            throw new CustomException(ErrorCode.FAIL_WITHDRAW);
        }

        return cnt;
    }


    @Override
    @Transactional(readOnly=true)
    public GetMemberRes findMemberByCarNumber(String carNumber) { return memberMapper.selectMemberByCarNumber(carNumber);}

    @Override
    public PutMemberReq modifyMemberDetails(PutMemberReq putMemberReq) {

        String hashedPassword = passwordEncoder.encode(putMemberReq.getPassword());

        putMemberReq.setPassword(hashedPassword);

        int cnt = memberMapper.updateMemberDetails(putMemberReq);

        if (cnt == 0) {

            throw new CustomException(ErrorCode.FAIL_MODIFY_USER_DETIALS);

        }

        return putMemberReq;
    }

    @Override
    @Transactional
    public void modifyCarNumber(PostMemberReq postMemberReq) {

        int cnt = memberMapper.updateCarNumber(postMemberReq);

        if(cnt == 0){

            throw new CustomException(ErrorCode.FIND_DUPLICATED_CARNUMBER);

        }

        parkingMapper.updateMemberId(postMemberReq);

    }

    @Override
    @Transactional
    public void removeCarNumber(String username) {

        String carNumber = memberMapper.selectMemberByUsername(username).getCarNumber();

        if(carNumber == null){
            throw new CustomException(ErrorCode.INVALID_CAR_STATUS);
        }

        parkingMapper.deleteMemberId(carNumber);

        memberMapper.deleteCarNumber(username);

    }

    @Override
    public void modifyFCM(PostMemberReq postMemberReq) {
        PostMemberReq modifiedReq = PostMemberReq.builder()
                .fcmToken(postMemberReq.getFcmToken())
                .username(postMemberReq.getUsername())
                .build();
        int cnt = memberMapper.updateFCM(modifiedReq);
        if(cnt == 0){
            throw new CustomException(ErrorCode.FAIL_UPDATE_TOKEN);
        }
    }

}
