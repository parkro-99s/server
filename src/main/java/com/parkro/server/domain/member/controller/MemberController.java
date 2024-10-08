package com.parkro.server.domain.member.controller;

import com.parkro.server.domain.member.dto.*;
import com.parkro.server.domain.member.service.MemberService;
import com.parkro.server.exception.CustomException;
import com.parkro.server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 회원
 *
 * @author 양재혁
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.07.25  양재혁       최초 생성
 * 2024.07.26  양재혁       아이디 중복 조회 API 생성
 * 2024.07.27  양재혁       회원 가입 API 생성
 * 2024.07.28  양재혁       회원 정보 수정 API 생성
 * 2024.07.28  양재혁       회원 탈퇴 API 생성
 * 2024.07.28  양재혁       회원 정보 조회 API 생성
 * 2024.07.29  양재혁       로그인 API 생성
 * 2024.07.29  양재혁       차량 등록 API 생성
 * 2024.07.29  양재혁       차량 삭제 API 생성
 * </pre>
 * @since 2024.07.25
 */
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  @GetMapping()
  public ResponseEntity<String> usernameDetails(@RequestParam("user") String username) {

    memberService.findUsername(username);

    return ResponseEntity.ok("사용 가능한 아이디 입니다.");
  }

  @PostMapping("/sign-up")
  @Validated
  public ResponseEntity memberSignUp(@Valid @RequestBody PostMemberReq postMemberReq, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {

      Map<String, String> errorMessages = new LinkedHashMap<>();

      bindingResult.getAllErrors().forEach(error -> {
        if (error instanceof FieldError) {
          String field = ((FieldError) error).getField();
          String message = error.getDefaultMessage();
          errorMessages.put(field, message);
        } else {
          errorMessages.put("error", error.getDefaultMessage());
        }
      });

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    memberService.addMember(postMemberReq);

    return ResponseEntity.ok("회원 가입이 완료되었습니다.");

  }

  @PatchMapping("/{username}")
  public ResponseEntity<Integer> usernameRemove(@PathVariable String username) {

    return ResponseEntity.ok(memberService.removeMember(username));

  }

  @PostMapping("/sign-in")
  public ResponseEntity<PostSignInRes> memberSignIn(@RequestHeader(value = "FCM-TOKEN") String fcmToken, @RequestBody PostMemberReq postMemberReq) {

    PostMemberRes postMemberRes = memberService.signInMember(postMemberReq);

    PostSignInRes postSignInRes = postMemberRes.getPostSignInRes();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + postMemberRes.getToken());

    postMemberReq.setFcmToken(fcmToken);
    memberService.modifyFCM(postMemberReq);

    return ResponseEntity.status(HttpStatus.OK)
            .headers(httpHeaders)
            .body(postSignInRes);
  }


  @GetMapping("/{username}")
  public ResponseEntity<GetMemberRes> memberDetails(@PathVariable String username) {

    return ResponseEntity.ok(memberService.findMember(username));

  }

  @PutMapping("/{username}")
  @Validated
  public ResponseEntity<?> memberModify(@PathVariable String username, @Valid @RequestBody PutMemberReq putMemberReq, BindingResult bindingResult) {
    if (!username.equals(putMemberReq.getUsername())) {
      throw new CustomException(ErrorCode.FIND_FAIL_USER_ID);
    }

    if (bindingResult.hasErrors()) {
      Map<String, String> errorMessages = new LinkedHashMap<>();

      bindingResult.getAllErrors().forEach(error -> {
        if (error instanceof FieldError) {
          String field = ((FieldError) error).getField();
          String message = error.getDefaultMessage();
          errorMessages.put(field, message);
        } else {
          errorMessages.put("error", error.getDefaultMessage());
        }
      });

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    PutMemberReq updatedMemberReq = memberService.modifyMemberDetails(putMemberReq);

    return ResponseEntity.ok(updatedMemberReq.getCarProfile());
  }

  @PatchMapping("/car")
  public ResponseEntity<String> carNumberModify(@RequestBody PostMemberReq postMemberReq) {

    memberService.modifyCarNumber(postMemberReq);

    return ResponseEntity.ok("차량 번호 등록이 완료 되었습니다.");
  }

  @PatchMapping("/{username}/car")
  public ResponseEntity<String> carNumberRemove(@PathVariable String username) {
    memberService.removeCarNumber(username);
    return ResponseEntity.ok("차량이 삭제 되었습니다.");
  }

}
