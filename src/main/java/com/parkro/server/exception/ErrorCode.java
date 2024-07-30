package com.parkro.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* 400 */
    FIND_FAIL_USER_ID(400, "존재하지 않는 유저입니다."),
    FAIL_SIGN_IN(400, "로그인에 실패했습니다."),
    INVALID_PAYMENT_CANCELLATION(400, "결제하지 않은 차량입니다."),
    FAIL_MODIFY_USER_DETIALS(400, "누락 되거나 유효성 검사를 통과 하지 못한 항목이 있습니다."),
    FAIL_UPDATE_TOKEN(400, "FCM Token을 갱신하지 못했습니다."),

    /* 404 */
    FAIL_WITHDRAW(404, "회원 탈퇴에 실패했습니다."),
    RECEIPT_NOT_FOUND(404, "영수증 정보를 찾을 수 없습니다."),
    FAIL_DELETE_PARKING_(404, "주차 내역 삭제에 실패했습니다."),
    INVALID_CAR_STATUS(404,"등록된 차량이 없습니다."),
    INVALID_COUPON_ID(404,"해당 하는 쿠폰 아이디가 없습니다."),

    /* 409 */
    FIND_DUPLICATED_USERNAME(409, "중복된 유저 아이디 입니다."),
    INVALID_PARKING_STATUS(409, "정산되지 않거나, 이미 출차한 차량입니다."),
    FIND_FAIL_PARKING_INFO(409, "현재 주차된 차량이 없습니다."),
    FIND_FAIL_PARKING_LIST(409, "주차 내역이 없습니다."),
    FIND_DUPLICATED_CARNUMBER(409,"이미 차량이 등록되었거나 중복된 차량이 있습니다."),

    /* 500 */
    DATABASE_ERROR(500, "데이터베이스 오류가 발생했습니다."),
    COUPON_ERROR(500, "쿠폰 발급에 실패했습니다.");

    private final int status;
    private final String message;
}
