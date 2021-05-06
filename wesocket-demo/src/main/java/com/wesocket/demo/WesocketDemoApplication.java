package com.wesocket.demo;

import com.github.lazyboyl.websocket.integrate.EnableWebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableWebSocketServer(webSocketScanPackage = {"com.wesocket.demo.controller","com.wesocket.demo.security","com.wesocket.demo.exception"})
public class WesocketDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WesocketDemoApplication.class, args);
    }

}
