package com.vue.demo.vue;

import com.github.lazyboyl.websocket.integrate.EnableWebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableWebSocketServer(webSocketScanPackage = {"com.vue.demo.vue"})
public class VueDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueDemoApplication.class, args);
    }

}
