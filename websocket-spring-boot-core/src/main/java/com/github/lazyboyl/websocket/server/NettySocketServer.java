package com.github.lazyboyl.websocket.server;

import com.github.lazyboyl.websocket.server.channel.DispatchHandler;
import com.github.lazyboyl.websocket.server.channel.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.BeansException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * @author linzf
 * @since 2020/7/13
 * 类描述：
 */
@Component
public class NettySocketServer implements ApplicationContextAware {

    /**
     * 定义spring的ApplicationContext对象
     */
    private static ApplicationContext ac = null;

    /**
     * 存放所有的socketId的集合
     */
    public static ChannelGroup channelGroup = null;

    @PostConstruct
    public void start() throws InterruptedException {
        initWebSocketConfig();
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
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
                        // 新增ChunkedHandler，主要作用是支持异步发送大的码流（例如大文件传输），但是不占用过多的内存，防止发生java内存溢出错误
                        p.addLast(new ChunkedWriteHandler());
                        p.addLast(new DispatchHandler());
                        p.addLast(new WebSocketHandler());
                    }
                });
        ChannelFuture f = bootstrap.bind(new InetSocketAddress(8399)).sync();
        f.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务启动成功");
            } else {
                System.out.println("服务启动失败");
            }
        });
        f.channel().closeFuture().sync();
    }

    /**
     * 功能描述： 初始化bean的配置
     */
    protected void initWebSocketConfig() {
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }
}
