package com.top.mini.happy.down.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;


/**
 * @author xugang
 */
@Component
public class MyWebSocketServerHandler extends ChannelInboundHandlerAdapter {

    private WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(null, null, false);


    /**
     * 客户端链接成功
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    /**
     * 客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelRegistry.unRegister(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    public void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

        //关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            SocketSessionDTO session = ChannelRegistry.getSession(ctx);
            session.getWebSocketServerHandshaker().close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            ChannelRegistry.unRegister(ctx);
            return;
        }
        //ping请求
        if (frame instanceof PingWebSocketFrame) {

            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));

            return;
        }
        //只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {

            throw new Exception("仅支持文本格式");
        }

        //客服端发送过来的消息


        String request = ((TextWebSocketFrame) frame).text();

        JSONObject jsonObject = null;

        try {
            jsonObject = JSONObject.parseObject(request);
        } catch (Exception e) {
        }
        if (jsonObject == null) {

            return;
        }
        push(ctx, "发送消息");
    }

    /**
     * 第一次请求是http请求，请求头包括ws的信息
     */
    public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {


        if (!req.decoderResult().isSuccess()) {

            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手链接
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            //不支持
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            /**
             * 在这之后才能发送消息正常通讯
             */
            handshaker.handshake(ctx.channel(), req);
            ChannelRegistry.register(new SocketSessionDTO(ctx, handshaker));
        }

    }


    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {


        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
    }


    //异常处理，netty默认是关闭channel
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        //输出日志
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 推送单个
     */
    protected void push(ChannelHandlerContext ctx, String message) {

        TextWebSocketFrame tws = new TextWebSocketFrame(message);
        ctx.channel().writeAndFlush(tws);

    }


}