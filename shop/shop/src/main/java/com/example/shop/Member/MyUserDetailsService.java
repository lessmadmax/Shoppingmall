package com.example.shop.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Autowired
    public MyUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = memberRepository.findByUsername(username);
        if (result.isEmpty()){
            throw new UsernameNotFoundException("This id does not exist.");
        }
        var user = result.get();
        List<GrantedAuthority> authList = new ArrayList<>(); //
        authList.add(new SimpleGrantedAuthority("GeneralUser"));
        var a = new CustomUser(user.getUsername(), user.getPassword(), authList);
        a.displayName = user.getDisplayName();
        return a;
    }

}

class CustomUser extends User{

    public String displayName;

    public CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities){
        /*
    Generic : 데이터 타입을 일반화하는 기능. 제네릭을 사용하면 데이터 타입을 컴파일 시점에 지정할 수 있음.

    Collection : 컬렉션 프레임워크에서 제공하는 인터페이스이다. 리스트, 셋, 등 다양한 자료구조의 상위 인터페이스이다.

    와일드카드(?) : 제네릭에서 특정 타입의 불확실성을 표현하는 키워드임.

    Collection<? extends GrantedAuthority> : GrantedAuthority 또는 그 자식 타입을 담은 컬렉션이라는 뜻.

    여기서 Collection 객체로 사용하는 이유는, authorities를 다양한 구현체로 사용하기 위함이다.

    구현체란, 인터페이스나 추상 클래스에서 정의한 기능을 실제로 구현한 클래스이다.
        */
        super(username, password, authorities);
    }

}