package com.example.shop;

import java.time.ZonedDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {
    //서버접속하면 안녕하세요 보내기
    @GetMapping("/")
    String hello(){
        return "index.html";
    }

    @GetMapping("/date")
    @ResponseBody
    String date(){
        return ZonedDateTime.now().toString();
    }
}
