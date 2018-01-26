package com.top.mini.happy.down.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xugang on 18/1/17.
 */
@Component
public class NettySever {

    private static final int PORT = 7002;

    private static final int MAX_HTTP_LENGTH = 65536;

    private static final int BACK_LOG = 1024;

    private static final int RCV_BUF_SIZE = 592048;

    @PostConstruct
    public void init() {
        /**
         * 若同步将阻塞spring初始化主线程
         */
        Thread initNetty = new Thread(new InitNetty());
        initNetty.start();
    }

    private class InitNetty implements Runnable{
        @Override
        public void run() {
            /**
             * Boss线程：由这个线程池提供的线程是boss种类的，用于创建、连接、绑定socket,然后把这些socket传给worker线程池。
             * 在服务器端每个监听的socket都有一个boss线程来处理。在客户端，只有一个boss线程来处理所有的socket。
             */
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            /**
             * Worker线程：Worker线程执行所有的异步I/O，即处理操作
             */
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                /**
                 * ServerBootstrap 启动NIO服务的辅助启动类,负责初始话netty服务器，并且开始监听端口的socket请求
                 */
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap
                        .group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, BACK_LOG)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(RCV_BUF_SIZE))
                        /**
                         * 对出入数据进行业务操作
                         */
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline()
                                        //请求和应答消息解码
                                        .addLast("http-codec", new HttpServerCodec())
                                        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
                                        .addLast("aggregator", new HttpObjectAggregator(MAX_HTTP_LENGTH))
                                        // ChunkedWriteHandler：向客户端发送HTML5文件
                                        .addLast("http-chunked", new ChunkedWriteHandler())
                                        .addLast("handler", new MyWebSocketServerHandler());
                            }
                        });
                Channel ch = serverBootstrap.bind(PORT).sync().channel();
                ch.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        }
    }

}
