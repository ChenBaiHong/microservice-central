package com.baimicro.central.websocket.standard;

import com.baimicro.central.websocket.model.RequestFrame;
import com.baimicro.central.websocket.model.Result;
import com.baimicro.central.websocket.model.RpcServiceRequest;
import com.baimicro.central.websocket.pojo.PojoEndpointServer;
import com.baimicro.central.websocket.pojo.StoreRpcServiceInstance;
import com.baimicro.central.websocket.protocol.RpcDecoder;
import com.baimicro.central.websocket.protocol.RpcEncoder;
import com.baimicro.central.websocket.protocol.serialize.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @project: hualida-scada
 * @author: chen.baihoo
 * @date: 2020/8/31
 * @Description: TODO
 * version 0.1
 */
@Slf4j
public class TcpServerHandler extends SimpleChannelInboundHandler<RequestFrame> {

    private PojoEndpointServer pojoEndpointServer;

    private ChannelHandlerContext ctx;

    /**
     * @param pojoEndpointServer
     */
    protected TcpServerHandler(PojoEndpointServer pojoEndpointServer) {
        this.pojoEndpointServer = pojoEndpointServer;
    }

    /**
     * @param channelHandlerContext
     * @param requestFrame          解析的响应报文帧
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestFrame requestFrame) throws Exception {
        pojoEndpointServer.doBefore(ctx);
        pojoEndpointServer.doOnMessageFrame(ctx.channel(), requestFrame);
    }

    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    protected void channelRead1(ChannelHandlerContext ctx, RpcServiceRequest msg) throws Exception {
        ChannelPipeline pipeline = ctx.pipeline();
        Result<?> result;
        if (msg.getServiceName() != null && msg.getMethodName() != null) {
            if (pipeline.get("rpcEncoder") == null) {
                pipeline.addLast("rpcEncoder", new RpcEncoder(Result.class, new KryoSerializer(), 1000));
            }
            if (pipeline.get("httpServerCodec") != null)
                pipeline.remove("httpServerCodec");
            // TODO 注释该代码，RPC解析会有问题
//            if (pipeline.remove("httpObjectAggregator") != null)
//                pipeline.remove("httpObjectAggregator");
            String serviceName = msg.getServiceName();
            String methodName = msg.getMethodName();
            Object object = StoreRpcServiceInstance.get(serviceName);
            Method method = object.getClass().getDeclaredMethod(methodName, msg.getParameterTypes());
            method.setAccessible(true);
            Object res = method.invoke(object, msg.getParameters());
            if (res instanceof Result) {
                result = (Result<?>) res;
            } else {
                result = new Result<>();
            }
        } else {
            result = new Result<>(1, "无法远程调用 RPC 服务方法，原因：可能 beanName 或者 methodName 为空");
        }
        result.setRequestId(msg.getRequestId());
        pipeline.channel().writeAndFlush(result);
        pipeline.channel().flush();
        System.out.println("rpc server reading client send message:" + msg);
    }

    /**
     * 自定义控制解析接收内容
     *
     * @param msg
     * @return
     * @throws Exception
     */
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        try {
            if (msg instanceof ByteBuf) {
                ByteBuf byteBuf = (ByteBuf) msg;
                // Q/GDW 1376.1 电信息采集系统通信协议格式
                if (byteBuf.getByte(0) == 0x68 && byteBuf.getByte(5) == 0x68 && byteBuf.getByte(byteBuf.readableBytes() - 1) == 0x16) {
                    /*byte[] req = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(req);
                    int[] msgArray = TheMoronicCowmpouterUtil.byteToInt(req);
                    PacketFrame packetFrame = new PacketFrame();
                    String analysisContent = AnalysisPacket.analysis(msgArray, packetFrame);
                    RequestFrame requestFrame = new RequestFrame(packetFrame, analysisContent);
                    channelRead0(ctx, requestFrame);*/
                } else {
                    RpcServiceRequest serviceRequest = new RpcDecoder<>(RpcServiceRequest.class, new KryoSerializer(), 1000).decode(byteBuf);
                    if (serviceRequest != null)
                        channelRead1(ctx, serviceRequest);
                }
            }
        } catch (Exception e) {
            log.info("解析报文出错，致命原因: " + e);
        }
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        pojoEndpointServer.doOnError(ctx.channel(), cause);
        System.out.println("ScadaTcpServerHandler 出现错误");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        pojoEndpointServer.doOnEvent(ctx.channel(), evt);
        System.out.println("ScadaTcpServerHandler 出现异常");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        pojoEndpointServer.doOnClose(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        pojoEndpointServer.doOnOpen(ctx.channel());
        System.out.println("tcp 网络隧道 激活");
    }
}
