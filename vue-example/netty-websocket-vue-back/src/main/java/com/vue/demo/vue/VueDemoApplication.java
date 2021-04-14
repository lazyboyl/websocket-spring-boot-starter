package com.vue.demo.vue;

import com.github.lazyboyl.websocket.integrate.EnableWebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableWebSocketServer(webSocketScanPackage = {"com.vue.demo.vue"})
@ComponentScan({"com.lazyboyl.starter.demo","com.vue.demo.vue"})
public class VueDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueDemoApplication.class, args);
    }

}
