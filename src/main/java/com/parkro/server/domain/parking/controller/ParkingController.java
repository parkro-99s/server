package com.parkro.server.domain.parking.controller;

import com.parkro.server.domain.parking.dto.*;
import com.parkro.server.domain.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주차
 *
 * @author 김민정
 * @since 2024.07.29
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.07.26  김민정      최초 생성
 * 2024.07.27  김민정      입차 API
 * 2024.07.28  김민정      출차 API
 * 2024.07.28  김민정      주차 정산(전) 정보 조회 API
 * 2024.07.28  김민정      나의 주차 내역 목록 조회 API
 * 2024.07.28  김민정      주차 내역 삭제 API
 * 2024.07.28  김민정      [관리자] 주차 내역 상세 조회 API
 * 2024.07.28  김민정      [관리자] 지점별 주차 내역 목록 조회 API
 * 2024.07.28  김민정      [관리자] 결제 완료 API
 * </pre>
 */
@RestController
@RequiredArgsConstructor
public class ParkingController {

  private final ParkingService parkingService;

  // 입차
  @PostMapping("/parking/in")
  public ResponseEntity<Integer> parkingAdd(@RequestBody PostParkingReq req) {
    return ResponseEntity.ok(parkingService.addParking(req));

  }

  // 출차
  @PatchMapping("/parking/out")
  public ResponseEntity<Integer> parkingOutModify(@RequestBody PatchParkingReq req) {
    return ResponseEntity.ok(parkingService.modifyParkingOut(req));
  }

  // 주차 정산(전) 정보 조회
  @GetMapping("/parking")
  public ResponseEntity<GetParkingPayRes> parkingPayDetails(@RequestParam String username) {
    return ResponseEntity.ok(parkingService.findParkingPay(username));
  }

  // 나의 주차 내역 목록 조회
  @GetMapping("/parking/list")
  public ResponseEntity<List<GetParkingRes>> myParkingList(@RequestParam String username, @RequestParam Integer page) {
    GetParkingReq req = GetParkingReq.builder().page(page).build();
    return ResponseEntity.ok(parkingService.findMyParkingList(username, req));
  }

  // 주차 내역 삭제
  @DeleteMapping("/parking/{parkingId}")
  public ResponseEntity<Integer> parkingRemove(@PathVariable Integer parkingId) {
    return ResponseEntity.ok(parkingService.removeParking(parkingId));
  }

  // [관리자] 주차 내역 상세 조회
  @GetMapping("/admin/parking/detail/{parkingId}")
  public ResponseEntity<GetParkingDetailRes> adminParkingDetails(@PathVariable Integer parkingId) {
    return ResponseEntity.ok(parkingService.findAdminParkingDetails(parkingId));
  }

  // [관리자] 지점별 주차 내역 목록 조회
  @GetMapping("/admin/parking/list")
  public ResponseEntity<List<GetParkingRes>> adminParkingList(@RequestParam Integer storeId,
                                                              @RequestParam Integer parkingLotId,
                                                              @RequestParam String date,
                                                              @RequestParam(required = false) String car,
                                                              @RequestParam Integer page) {

    GetParkingReq req = GetParkingReq.builder()
            .storeId(storeId)
            .parkingLotId(parkingLotId)
            .date(date)
            .carNumber(car)
            .page(page).build();
    return ResponseEntity.ok(parkingService.findParkingListByStore(req));
  }

  // [관리자] 결제 완료
  @PatchMapping("/admin/parking/out/{parkingId}")
  public ResponseEntity<Integer> adminParkingOutModify(@PathVariable Integer parkingId) {
    return ResponseEntity.ok(parkingService.modifyParkingOutById(parkingId));
  }

}
