package com.wesocket.demo.controller;

import com.github.lazyboyl.websocket.annotation.WebSocketController;
import com.github.lazyboyl.websocket.annotation.WebSocketRequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：
 */
@WebSocketController
@WebSocketRequestMapping("demo")
public class DemoController {

    @WebSocketRequestMapping("helloWord")
    public String helloWord(){
        return "你好阿前端";
    }


    @WebSocketRequestMapping("helloWord1")
    public Map<String,Object> helloWord1(String abc){
        System.out.println("----" + abc);
        Map<String,Object> m = new HashMap<>();
        m.put("aaaa",abc);
        List<String> aaa = new ArrayList<>();
        aaa.add("aaaaa");
        aaa.add("ccccc");
        m.put("cccc",aaa);
        return m;
    }

    @WebSocketRequestMapping("helloWord2")
    public void helloWord2(){
        System.out.println("你好阿我被响应了！");
    }

}
