package com.top.mini.happy.down.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xugang on 18/1/17.
 */
@Component
public class ChannelRegistry {
    /**
     * 存放所有的ChannelHandlerContext
     */
    private final static Map<ChannelId, SocketSessionDTO> channelMap = new ConcurrentHashMap<>();

    public static void register(SocketSessionDTO session){
        channelMap.put(session.getChannelHandlerContext().channel().id(),session);
    }

    public static void unRegister(ChannelHandlerContext ctx){
        channelMap.remove(ctx.channel().id());
    }

    public static SocketSessionDTO getSession(ChannelHandlerContext ctx){
        return channelMap.get(ctx.channel().id());
    }



}
