package com.example.securitytest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
// 데이터 베이스 정보를 가져오기 위한 클래스
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClubMemberDto {
    private String email; //해당 db에서 id의 역할을 하는 컬럼
    private String password;
    private String name;
    private boolean fromSocial; //sns연동 로그인 구분
//    사용자 권한을 저장하는 변수
//    만약 db에 컬럼이 따로 존재할 경우 일반 String 타입으로 사용해도 상관없다
    private Set<ClubMemberRole> roleSet = new HashSet<>();

//    사용자 권한을 추가하기 위한 메서드
//    만약 데이터 베이스에 컬럼이 따로 존재 시 해당 메서드는 필요없다.
    public void addMemberRole(ClubMemberRole clubMemberRole){
        roleSet.add(clubMemberRole);
    }
}
