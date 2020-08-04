package com.wesocket.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2020/8/4
 * 类描述：
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("testAbc")
    public String testAbc(){
        return "asas";
    }

}
