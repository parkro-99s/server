package com.parkro.server.domain.parking.service;

import com.parkro.server.domain.member.dto.GetMemberRes;
import com.parkro.server.domain.member.service.MemberService;
import com.parkro.server.domain.parking.dto.PostParkingReq;
import com.parkro.server.domain.parking.mapper.ParkingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingMapper parkingMapper;
    private final MemberService memberService;

    // 입차
    @Override
    @Transactional
    public Integer addParking(PostParkingReq req) {

        // 입차한 차 번호로 가입된 유저 조회
        GetMemberRes member = memberService.findMemberByCarNumber(req.getCarNumber());
        if (member == null) {
            req.setMemberId(null);
        } else {
            req.setMemberId(member.getMemberId());
        }
        parkingMapper.insertParking(req);
        return req.getParkingId();
    }
}
