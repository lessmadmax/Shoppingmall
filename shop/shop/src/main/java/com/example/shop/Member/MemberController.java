package com.example.shop.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.Authenticator;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    //오브젝트 미리 뽑기

    //회원가입 기능 만들기
    //1. 회원가입 페이지 만들기
    //2. 제출 버튼 누르면 테이블에 업데이트
    //3. 로그인 페이지에서 아이디 비번 입력하면 로그인

    @GetMapping("/register")
    String register(Authentication auth){
        if (auth != null && auth.isAuthenticated()){
            return "redirect:/list";
        }
            return "register.html";

    }

    @PostMapping("/registerPost")
    String registerPost(String username, String password, String displayName) throws Exception {
        memberService.saveMember(username, password, displayName);
        return "redirect:/list";
    }

    @GetMapping("/login")
    String login(){
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth, Model model){
        CustomUser result = (CustomUser) auth.getPrincipal();
        String displayname = result.displayName;
        model.addAttribute("displayname", displayname);
        return "mypage.html";
    }

    @GetMapping("/user/1")
    @ResponseBody
    public MemberDto getUser(){
        var a = memberRepository.findById(1L);
        var result = a.get();
        var data = new MemberDto(result.getUsername(), result.getDisplayName());
        return data;
    }

}

//DTO : Data Transfer Object이다. 클라이언트와 서버가
//데이터를 주고받을 때 사용하는 객체이다.
class MemberDto {
    public String username;
    public String displayName;
    MemberDto(String a, String b){
        this.username = a;
        this.displayName = b;
    }
}