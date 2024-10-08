package com.parkro.server.domain.payment.service;

import com.parkro.server.domain.alarm.dto.PaymentCancelDTO;
import com.parkro.server.domain.parking.service.ParkingService;
import com.parkro.server.domain.payment.mapper.PaymentMapper;
import com.parkro.server.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.parkro.server.exception.ErrorCode.INVALID_PAYMENT_CANCELLATION;

/**
 * 결제 취소를 위한 지연 작업 관련 로직
 *
 * @author 김지수
 * @since 2024.07.26
 *
 * <pre>
 * 수정일자       수정자        수정내용
 * ------------ --------    ---------------------------
 * 2024.07.26   김지수      최초 생성
 * 2024.07.26   김지수      비동기로 수행하는 결제 취소 작업(modifyCancelledDate) 호출 API
 * 2024.07.26   김지수      결제 취소 API
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentSchedulerServiceImpl implements PaymentSchedulerService {

  private final ParkingService parkingService;
  private final PaymentMapper paymentMapper;
  private final ApplicationEventPublisher eventPublisher;

  /**
   * 비동기로 수행하는 결제 취소 작업(modifyCancelledDate) 호출 메서드
   *
   * @param parkingId
   * @param paymentId
   * @return 작업 완료를 나타내는 {@link CompletableFuture<Void>}
   */
  @Async
  @Override
  public CompletableFuture<Void> schedulerModifyCancelledDate(Integer parkingId, Integer paymentId, String fcmToken) {
    log.info("결제 취소 카운트 시작");
    try {
      TimeUnit.MINUTES.sleep(10);
      modifyCancelledDate(parkingId, paymentId, fcmToken);
    } catch (InterruptedException e) {
      log.error("Error during sleep: " + e);
    }

    return CompletableFuture.completedFuture(null);
  }

  /**
   * 결제 취소
   *
   * @param parkingId
   * @param paymentId
   */
  @Override
  public void modifyCancelledDate(Integer parkingId, Integer paymentId, String fcmToken) {
    String curr_status = parkingService.findParkingByParkingId(parkingId).getStatus();
    // 현재 출차한 상태라면 결제 취소 로직 수행하지 않음
    if (curr_status.equals("EXIT")) return;
    // 현재 결제 전 상태라면 예외 처리
    if (curr_status.equals("ENTRANCE")) throw new CustomException(INVALID_PAYMENT_CANCELLATION);

    // 결제 취소 수행
    paymentMapper.updateCancelledDate(paymentId);

    // 주차된 차량의 상태 업데이트
    parkingService.modifyParkingStatusEnter(parkingId);

    // FCM 알림 - 결제 취소
    eventPublisher.publishEvent(new PaymentCancelDTO(fcmToken));

    log.info("parking_id {}번, payment_id {}번 결제 내역 취소 완료", parkingId, paymentId);
  }
}
