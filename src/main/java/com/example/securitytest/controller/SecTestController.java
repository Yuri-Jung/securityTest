package com.example.securitytest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("/sec") //기본 주소를 sec로 쓰겠다는 의미
@Controller

public class SecTestController {
    @RequestMapping("")
    public String index(){
        return "sec/index";
    }

    @RequestMapping("/all")
    public String all(){
        return "sec/all";
    }

    @RequestMapping("/member")
    public String member(){
        return "sec/member";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "sec/admin";
    }



}
