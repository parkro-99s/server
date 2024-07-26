package com.parkro.server.domain.parking.mapper;

import com.parkro.server.domain.parking.dto.GetParkingRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingMapper {

  // parkingId로 주차 정보 조회
  GetParkingRes selectParkingByParkingId(Integer parkingId);
}
