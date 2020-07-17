websocket-spring-boot-starter [![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
===================================
### 简介
在应用程序中你只需要几行代码就可以快速的构建websocket服务。
### 要求
- jdk版本为1.8或1.8+
- spring-boot版本为2.0.1.RELEASE+
- netty版本为4.1.42.Final

### example例子
[基于vue的前后端分离的实现的例子](https://github.com/lazyboyl/websocket-spring-boot-starter/tree/master/vue-example)
### 快速开始

#### 1、添加依赖:

```xml
     <dependency>
         <groupId>com.github.lazyboyl</groupId>
         <artifactId>websocket-spring-boot-starter</artifactId>
         <version>1.0.1.RELEASE</version>
     </dependency>
```

#### 2、添加注解
在spring boot的启动类上加上`@EnableWebSocketServer`注解，并在该注解上设置好需要扫描的路径。
```java
@SpringBootApplication
@EnableWebSocketServer(webSocketScanPackage = {"com.vue.demo.vue"})
public class VueDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueDemoApplication.class, args);
    }

}
```
#### 3、编写第一个后端响应请求
你可以像写springMvc一样写websocket，完全不用像以前一样要做各种的配置和处理。
```java
/**
 * @author linzf
 * @since 2020/7/15
 * 类描述： 第一个请求类
 */
@WebSocketController
@WebSocketRequestMapping("/user/")
public class UserController {
    
    /**
     * 这个userService就是我们平时在spring里面写的service
     */
    @Autowired
    private UserService userService;

    /**
     * 功能描述： 模拟字段请求的方式来实现根据用户ID来获取用户数据
     *
     * @param userId 用户的流水ID
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserId")
    public UserVo getUserVoByUserId(String userId) {
        return userService.getUserVoByUserId(userId);
    }

}
```
#### 4、编写第一个拦截器
只需要实现`WebsocketSecurity`接口即可，`level`方法主要用于界定这个过滤器的执行的顺序，数字越大则级别越高，执行的排序也越高，`authentication`返回的值若为`true`则放行，反之则直接被拦截不再往下响应
```java
/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：请求拦截器
 */
public class WebsocketSecurityImpl implements WebsocketSecurity {

    /**
    * 这个authService就是我们平时在spring里面写的service
    **/
    @Autowired
    private AuthService authService;

    @Override
    public int level() {
        return 10;
    }

    @Override
    public Boolean authentication(ChannelHandlerContext ctx, SocketRequest socketRequest) {
        Boolean isPass = authService.authUrl(socketRequest.getUrl());
        System.out.println("----我在这里做了一个模拟的鉴权过程---");
        if(!isPass){
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objToJson(new SocketResponse(HttpResponseStatus.UNAUTHORIZED.code(), "授权不通过！"))));
        }
        return isPass;
    }
}
```
#### 4、编写第一个监听器
只需实现`WebSocketHandlerListenter`接口即可，`level`方法主要用于界定这个监听器的执行的顺序，数字越大则级别越高，执行的排序也越高，`channelInactive`当通道关闭的时候执行的方法，`handleShake`当websocket尝试握手的时候执行的方法。
```java
/**
 * @author linzf
 * @since 2020/7/14
 * 类描述： 通道关闭监听器
 */
public class WebSocketHandlerListenterImpl implements WebSocketHandlerListenter {

    /**
    * 这个webSocketCloseService就是我们平时在spring里面写的service
    **/
    @Autowired
    private WebSocketCloseService webSocketCloseService;


    @Override
    public int level() {
        return 0;
    }

    /**
     * 功能描述： 当浏览器端的通道关闭的时候的响应处理方法
     *
     * @param ctx 当前的通道对象
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("当前关闭的通道的id是：" + ctx.channel().id().asLongText());
        webSocketCloseService.removeChannel(ctx.channel().id().asLongText());
    }

    @Override
    public void handleShake(ChannelHandlerContext ctx) {
        System.out.println("当前开启的通道的id是：" + ctx.channel().id().asLongText());
    }
}
```
#### 5、编写第一个前端响应请求
前端采用的是vue响应的方式，不过本质上还是用的是浏览器的本身自带的websocket因此写法上没什么差别，只是此处在与后端进行websocket交互的
时候需要按照一定的格式往后端提交数据，格式如下：
```js
  {'url': 后端的请求地址, 'params': 请求的JSON数据}
```
例如本例中写了一个后端请求的地址是`/user/getUserVoByUserId`那我们此处的请求地址就是这个地址，同时后端的这个方法的入参是`userId`，那么我们这边的`params`的JSON数据
的格式就是`{'userId':'123'}`那么最终我们请求上来的数据格式如下：
```js
  {'url': '/user/getUserVoByUserId', 'params': {'userId':'123'}}
```
最后直接调用我们的websocket对象的send方法请求后端即可。
#### 6、编写前端的实现
```js
<template>
<div>
  <button @click="getUserVoByUserId">getUserVoByUserId</button>
</div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      websock: null,
      wsuri: 'ws://127.0.0.1:8399/wsVue'
    }
  },
  created () {
    this.initWebSocket()
  },
  methods: {
    getUserVoByUserId () {
      let actions = {'url': '/user/getUserVoByUserId/', 'params': {'userId': 'abc'}}
      this.websocketsend(JSON.stringify(actions))
    },
    initWebSocket () { // 初始化weosocket
      this.websock = new WebSocket(this.wsuri)
      this.websock.onmessage = this.websocketonmessage
      this.websock.onopen = this.websocketonopen
      this.websock.onerror = this.websocketonerror
      this.websock.onclose = this.websocketclose
    },
    websocketonopen () { // 连接建立之后执行send方法发送数据
    },
    websocketonerror () { // 连接建立失败重连
      console.log('--出错了-')
      this.initWebSocket()
    },
    websocketonmessage (e) { // 数据接收
      const redata = JSON.parse(e.data)
      console.log('后端发送过来的信息是：' + e.data)
    },
    websocketsend (Data) { // 数据发送
      if(this.websock.readyState === 1){
        this.websock.send(Data)
      } else {
        setTimeout(()=>{
          this.websock = new WebSocket(this.wsuri)
          this.websock.onmessage = this.websocketonmessage
          this.websock.onopen = this.websocketonopen
          this.websock.onerror = this.websocketonerror
          this.websock.onclose = this.websocketclose
          setTimeout(()=>{this.websocketsend(Data)},1000)
        },1000)
      }
    },
    websocketclose (e) { // 关闭
      console.log('断开连接', e)
    }
  }
}
</script>
```
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
---
### 版本更新
#### 1.0.1.RELEASE
- `@WebSocketController`注解的类支持构造方法的方式注入的支持。
- 新增`@WebSocketRequestParam`支持入参的自定义。
