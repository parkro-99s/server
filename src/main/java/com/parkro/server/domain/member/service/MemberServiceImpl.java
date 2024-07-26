package com.parkro.server.domain.member.service;

import com.parkro.server.domain.member.dto.GetMemberRes;
import com.parkro.server.domain.member.dto.PostMemberReq;
import com.parkro.server.domain.member.mapper.MemberMapper;
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

    @Transactional
    @Override
    public Optional<PostMemberReq> findUsername(String username) {

        Optional<PostMemberReq> optionalPostMemberReq = memberMapper.selectUsername(username);
        return optionalPostMemberReq;
    }

    @Override
    @Transactional
    public Integer addMember(PostMemberReq postMemberReq) {

        String hashedPassword = passwordEncoder.encode(postMemberReq.getPassword());
        PostMemberReq signUpMemberReq = PostMemberReq.builder().username(postMemberReq.getUsername()).password(hashedPassword).nickname(postMemberReq.getNickname()).phoneNumber(postMemberReq.getPhoneNumber()).carNumber(postMemberReq.getCarNumber()).build();
        return memberMapper.insertMember(signUpMemberReq);

    }

    @Override
    @Transactional
    public String signInMember(PostMemberReq postMemberReq) {

        Optional<PostMemberReq> memberOpt = memberMapper.selectUsername(postMemberReq.getUsername());

        if (memberOpt.isPresent()) {

            PostMemberReq member = memberOpt.get();

            if (passwordEncoder.matches(postMemberReq.getPassword(), member.getPassword())) {
                String token = jwtTokenProvider.createToken(member.getUsername(), Collections.singletonList(member.getRole()));
                return token;
            }
        }

        throw new CustomException(ErrorCode.FIND_FAIL_USER_ID);
    }

    @Override
    public GetMemberRes findMember(String username) {
        return memberMapper.selectUserByUsername(username);
    }
  
    @Override
    public Integer deleteMember(String username) {

        int cnt = memberMapper.deleteMember(username);

        if(cnt == 0){
            throw new CustomException(ErrorCode.FAIL_WITHDRAW);
        }

        return cnt;
    }


}
