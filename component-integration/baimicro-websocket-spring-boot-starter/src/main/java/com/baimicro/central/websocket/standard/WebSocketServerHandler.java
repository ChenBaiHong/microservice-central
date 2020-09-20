package com.baimicro.central.websocket.standard;

import com.baimicro.central.websocket.pojo.PojoEndpointServer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 服务处理
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private PojoEndpointServer pojoEndpointServer;

    /**
     * 支持 Springboot 集成改造
     *
     * @param pojoEndpointServer
     */
    public WebSocketServerHandler(PojoEndpointServer pojoEndpointServer) {
        this.pojoEndpointServer = pojoEndpointServer;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handleWebSocketFrame(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        pojoEndpointServer.doOnError(ctx.channel(), cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        pojoEndpointServer.doOnClose(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        pojoEndpointServer.doOnEvent(ctx.channel(), evt);
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (frame instanceof TextWebSocketFrame) {
            pojoEndpointServer.doOnMessage(ctx.channel(), frame);
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof CloseWebSocketFrame) {
            ctx.writeAndFlush(frame.retainedDuplicate()).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        if (frame instanceof BinaryWebSocketFrame) {
            pojoEndpointServer.doOnBinary(ctx.channel(), frame);
            return;
        }
        if (frame instanceof PongWebSocketFrame) {
            return;
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("WebSocketServerHandler channel 激活");
    }
}
