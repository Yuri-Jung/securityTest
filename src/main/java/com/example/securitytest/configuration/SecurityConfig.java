package com.example.securitytest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
// 스프링 시큐리티 설정을 위해서 WebSecurityConfigurerAdapter 클래스를 상속받아 사용함(2.6.x 버전까지)
//  인증과 인가에 관련된 api를 제공하는 클래스
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    스프링 시큐리티에서는 반드시 비밀번호를 암호화해서 사용해야 한다
//    BCryptPasswordEncoder 클래스를 사용하여 비밀번호를 암호화 해아한다.

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
//      HttpSecurity 클래스 : 스프링 시큐리티를 사용하는 웹 페이지에 접속할 경우 필요한 설정을 할 수 있음

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        필요한 설정을 진행
//        authorizeRequests() : 요청에 대한 보안 설정을 시작하는 메서드
//        authorizeHttpRequests() : http 요청에 대한 보안 설정을 시작하는 메서드
//
//        antMatchers(url) : 지정한 주소에 대한 url 주소에 대한 설정
//        permitAll() : 지정한 url에 대해서 사용 인가
//        hasRole(권한등급) : 지정한 등급의 사용자만 지정한 url에 대하여 사용 인가
//        hasAnyRole(권한등급) : 지정한 url에 대하여 지정한 등급 중 하나 이상을 가진 사용자만 접근 가능
//        isAnonymous() : 익명 사용자만 접근

//        isRememberMe() : Remember Me 인증을 통해서 접근 시에만 인가
//        isFullyAuthenticated() : Remember Me가 아닌 일반적인 인증 방법으로 로그인 시에만 접근
//        denyAll() : 접근 불가
//        principal() : 인증된 사용자의 사용자 정보 반환 (UserDetails 인터페이스를 구현한 클래스의 객체)
//        authentication() : 인증된 사용자의 인증 정보 반환(Authentication** 인터페이스를 구현한 클래스의 객체)
        http.authorizeRequests()
                .antMatchers("/sec/all").permitAll() // 주소에 대해 보안을 하지만 특정 주소에 대해서는 사용인가 부여
                .antMatchers("/sec/member").hasAnyRole("USER","ADMIN") //member는 user와 admin 접근 가능하다
                .antMatchers("/sec/admin").hasRole("ADMIN"); //admin 계정은 admin만 접근 가능하다

//        formLogin() : 기본 스프링 시큐리티 로그인 페이지 사용
//        로그인 페이지 사용 시 사용할 수 있는 추가 옵션
//        loginPage(url) : 사용자 정의 로그인 페이지 옵션
//        defaultSuccessUrl(url) : 로그인 성공 후 이동할 페이지 지정
//        failureUrl(url) : 로그인 실패 후 이동할 페이지 지정
//        usernameParameter(ID) : ID 파라미터명 설정
//        passwardParameter(PW) : 비밀번호 파라미터명 설정
//        loginProcessingUrl(url) : 로그인 Form Action Url 지정
//        successHandler(loginSuccessHandler()) : 로그인 성공 후 동작할 내용 설정
//        failureHandler(loginFailureHandler()) : 로그인 실패 후 동작할 내용 설정

        http.formLogin();
//        logout() : 로그아웃 기능 활성화, 기본적으로 로그아웃 페이지는 post방식으로만 동작한다.
//        로그아웃 페이지 사용시 사용할 수 있는 추가 옵션
//        logoutUrl(url) : 로그아웃 처리 페이지 지정
//        logoutSuccessUrl(url) : 로그아웃 성공 시 이동할 페이지 지정
//        deleteCookies('JSESSIONID','remember-me') : 로그아웃 시 쿠키 삭제
//        addLogoutHandler() : 로그 아웃시 동작할 내용 설정
//        logoutSuccessHandler() : 로그아웃 성공 시 동작할 내용 설정
        http.logout();
        
    }

    @Override
//    AuthenticationManagerBuilder : 인증정보 만들어줌
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("1111")).roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("1111")).roles("ADMIN");
    }
}
