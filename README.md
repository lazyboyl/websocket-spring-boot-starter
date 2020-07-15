websocket-spring-boot-starter [![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
===================================
### 简介
这是一个让使用webSocket像使用springMVC一样轻松的插件，你只需要一个简单的引入，就可以直接使用websocket，同时快速的集成到你的spring boot
项目中，而无需关心其他任何的集成。

### 要求
- jdk版本为1.8或1.8+

### 快速开始

- 添加依赖:

```xml
     <dependency>
         <groupId>com.github.lazyboyl</groupId>
         <artifactId>websocket-spring-boot-starter</artifactId>
         <version>0.0.1-SNAPSHOT</version>
     </dependency>
```

- 在spring boot的启动类上加上`@EnableWebSocketServer`注解，并在该注解上设置好需要扫描的路径。

```java
@SpringBootApplication
@EnableWebSocketServer(webSocketScanPackage = {"com.wesocket.demo.controller","com.wesocket.demo.security"})
public class WesocketDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WesocketDemoApplication.class, args);
    }

}
```
- 编写第一个响应请求
### 配置
| 属性  | 默认值 | 说明 
|---|---|---
|websocket.port|8399|websocket的端口号
|websocket.thread.boss|12|boss的线程数
|websocket.thread.work|12|工作线程数
|websocket.action|ws|websocket的响应的地址

### 通过application.yml 进行配置

```
websocket:
   port: 8399
   action: ws
   thread:
        boss: 12
        work: 12
```