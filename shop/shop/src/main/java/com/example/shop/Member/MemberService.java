package com.example.shop.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//1. 너무 짧은 아이디나 비번을 전송하는 경우 가입을 막으려면?

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveMember(String username,
                           String password,
                           String displayName) throws Exception {
        try {
            if (username.length() < 4 || password.length() < 8) {
                throw new Exception("너무 짧음");
            } else {
                var encoder = new BCryptPasswordEncoder();
                Member member = new Member();
                member.setUsername(username);
                member.setPassword(encoder.encode(password));
                member.setDisplayName(displayName);
                memberRepository.save(member);
            }
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username or display name already exists.");
        }

    }

}
