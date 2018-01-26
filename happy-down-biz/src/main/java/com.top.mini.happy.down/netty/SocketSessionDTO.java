package com.top.mini.happy.down.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xugang on 18/1/18.
 */
@Data
@AllArgsConstructor
public class SocketSessionDTO {
    private ChannelHandlerContext channelHandlerContext;
    private WebSocketServerHandshaker webSocketServerHandshaker;
}
