package com.github.lazyboyl.websocket.server;

import com.github.lazyboyl.websocket.annotation.WebSocketController;
import com.github.lazyboyl.websocket.factory.NettyDefaultBeanFactory;
import com.github.lazyboyl.websocket.factory.WebSocketControllerBeanFactory;
import com.github.lazyboyl.websocket.factory.WebsocketInterfaceBeanFactory;
import com.github.lazyboyl.websocket.listenter.WebSocketHandlerListenter;
import com.github.lazyboyl.websocket.security.WebsocketSecurity;
import com.github.lazyboyl.websocket.server.channel.DispatchHandler;
import com.github.lazyboyl.websocket.server.channel.WebSocketHandler;
import com.github.lazyboyl.websocket.util.NettyScanner;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * @author linzf
 * @since 2020/7/13
 * 类描述： websocket服务器启动主入口类
 */
@Component
public class NettySocketServer implements ApplicationContextAware {

    /**
     * 定义spring的ApplicationContext对象
     */
    private static ApplicationContext ac = null;

    /**
     * websocket的controller的工厂扫描类
     */
    public static WebSocketControllerBeanFactory webSocketControllerBeanFactory;

    /**
     * websocketSecurityBeanFactory的鉴权的工厂扫描类
     */
    public static WebsocketInterfaceBeanFactory websocketSecurityBeanFactory;

    /**
     * websocketHandlerListenterBeanFactory的监听的工厂扫描类
     */
    public static WebsocketInterfaceBeanFactory websocketHandlerListenterBeanFactory;

    /**
     * 日志对象
     */
    private static Logger log = LoggerFactory.getLogger(NettySocketServer.class);

    /**
     * 注入spring的环境对象
     */
    @Autowired
    private Environment environment;

    /**
     * websocket扫描包的位置
     */
    @Value("${webSocket.scan.package}")
    private String[] webSocketScanPackage;

    /**
     * websocket的端口
     */
    @Value("${websocket.port}")
    private Integer port;

    /**
     * netty的boss线程数
     */
    @Value("${websocket.thread.boss}")
    private Integer bossThread;

    /**
     * netty的work线程数
     */
    @Value("${websocket.thread.work}")
    private Integer workThread;

    /**
     * websocket的响应的地址默认为ws
     */
    @Value("${websocket.action}")
    private String action;

    @PostConstruct
    public void start() throws Exception {
        initWebSocketConfig();
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup(bossThread);
        EventLoopGroup work = new NioEventLoopGroup(workThread);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.group(boss, work)
                .handler(new LoggingHandler(String.valueOf(LogLevel.DEBUG)))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast("compressor", new HttpContentCompressor());
                        //HTTP 服务的解码器
                        p.addLast(new HttpServerCodec(4096, 8192, 1024 * 1024 * 10));
                        //HTTP 消息的合并处理
                        p.addLast(new HttpObjectAggregator(10 * 1024));
                        p.addLast(new WebSocketServerProtocolHandler("/" + action));
                        // 新增ChunkedHandler，主要作用是支持异步发送大的码流（例如大文件传输），但是不占用过多的内存，防止发生java内存溢出错误
                        p.addLast(new ChunkedWriteHandler());
                        p.addLast(new DispatchHandler());
                        p.addLast(new WebSocketHandler());

                    }
                });
        ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();
        f.addListener(future -> {
            if (future.isSuccess()) {
                log.info("------------------------端口" + port + "的服务启动成功------------------------");
            } else {
                log.info("------------------------端口" + port + "的服务启动失败------------------------");
            }
        });
        f.channel().closeFuture().sync();
    }

    /**
     * 功能描述： 初始化bean的配置
     *
     * @throws Exception 出错信息
     */
    protected void initWebSocketConfig() throws Exception {
        NettyScanner nettyScanner = new NettyScanner();
        for (String pack : webSocketScanPackage) {
            nettyScanner.initClasses(pack);
        }
        webSocketControllerBeanFactory = WebSocketControllerBeanFactory.getInstance();
        injectionBean(WebSocketController.class, webSocketControllerBeanFactory, nettyScanner);
        websocketSecurityBeanFactory = WebsocketInterfaceBeanFactory.getInstance();
        injectionBean(WebsocketSecurity.class, websocketSecurityBeanFactory, nettyScanner);
        websocketHandlerListenterBeanFactory = WebsocketInterfaceBeanFactory.getInstance();
        injectionBean(WebSocketHandlerListenter.class, websocketHandlerListenterBeanFactory, nettyScanner);
    }


    /**
     * 功能描述： 实现相应的bean的注入
     *
     * @param cls          待注入的类型的class
     * @param factory      相应的工程
     * @param nettyScanner 扫描对象
     * @throws Exception 出错信息
     */
    protected void injectionBean(Class cls, NettyDefaultBeanFactory factory, NettyScanner nettyScanner) throws Exception {
        if (cls.isAnnotation()) {
            for (Class c : nettyScanner.getAnnotationClasses(cls)) {
                factory.registerBean(c, environment);
            }
        } else if (cls.isInterface()) {
            for (Class c : nettyScanner.getInterfaceClasses(cls)) {
                factory.registerBean(c, environment);
            }
        }

    }

    /**
     * 功能描述： 根据bean的名称来获取相应的bean
     *
     * @param beanName bean的名称
     * @return 返回相应的实例化的bean
     */
    public static Object getBean(String beanName) {
        return ac.getBean(beanName);
    }

    /**
     * @param applicationContext applicationContext对象
     * @throws BeansException bean的出错信息
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }
}
