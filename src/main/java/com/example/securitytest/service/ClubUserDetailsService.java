package com.example.securitytest.service;

import com.example.securitytest.dto.ClubAuthMemberDto;
import com.example.securitytest.dto.ClubMemberDto;
import com.example.securitytest.dto.ClubMemberRole;
import com.example.securitytest.mapper.ClubMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

// UserDetailService 사용자 정보를 데이터베이스에서 가져올 경우 UserDetailsService를 상속받아
// loadUserByUsername() 메서드를 구현해야 한다.
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {
    private final ClubMemberMapper clubMemberMapper;


//    db에서 사용자 정보를 가져오려면 반드시 UserDetailsService의  loadUserByUsername()메서드를 구현해야한다.
//    loadUserByUsername()메서드는 UserDetails 인터페이스를 구현한 클래스 객체를 반환해야 한다.
//    UserDetails 인터페이스를 상속받아 구현한 클래스의 객체를 스프링 시큐리티에서 확인하여 인증된 사용자인지
//    아닌지 판단한다.
//    사용자가 로그인 페이지에서 로그인 시 스프링 시큐리티가 먼저 데이터를 받아서 loadUserByUsername 메서드에
//    사용자 id를 매개변수로 사용해서 실행한다.
//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        Mybatis의 mapper를 이용하여 사용자 정보를 가져온다
//        Optional : db사용 시 데이터 조회 후 데이터가 null이 들어올 경우 오류가 발생할 수 있는 부분을 안전하게
//        사용하기 위한 데이터 타입.

        Optional<ClubMemberDto> result = clubMemberMapper.findByEmail(username,false);

//        db에 해당 사용자 정보가 있는지 확인한다.
        if (result.isEmpty()){
            throw new UsernameNotFoundException("이메일 및 비밀번호를 확인하세요");
        }

//        Optional 타입에 저장된 정보를 가져온다.result.get(): 전체 데이터 다 가져온다
        ClubMemberDto member = result.get();

//        데이터 베이스에 등급 권한 정보 컬럼이 있으면 필요없는 내용
//        사용자 정보에 사용 등급 권한 설정
        member.addMemberRole(ClubMemberRole.USER);

//        로그인 인증 정보를 가지고 있는 ClubAuthMemberDto 클래스 타입의 객체 생성
//        매개변수로 DB에서 가져온 정보를 넘겨서 사용자가 입력한 사용자ID를 가지고 있는 로그인 인증된
//        객체가 생성이 된다.
        ClubAuthMemberDto clubAuthMember = new ClubAuthMemberDto(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
//                스프링 시큐리티에서 사용하는 권한 정보는 모두 'ROLE_권한' 형태로 되어있다.
//                SET방식으로 데이터를 집어넣어주겠다.
                member.getRoleSet().stream().map(role-> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet())
        );

        clubAuthMember.setName(member.getName());
        clubAuthMember.setFromSocial(member.isFromSocial());

//      로그인 인증 정보를 가지고 있는 객체를 반환 시 스프링 시큐리티가 처리해 줌.
        return clubAuthMember;
    }
}
