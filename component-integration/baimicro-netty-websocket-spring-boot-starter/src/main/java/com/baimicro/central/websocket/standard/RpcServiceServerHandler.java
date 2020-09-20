package com.baimicro.central.websocket.standard;

import com.baimicro.central.websocket.model.Result;
import com.baimicro.central.websocket.model.RpcServiceRequest;
import com.baimicro.central.websocket.pojo.PojoEndpointServer;
import com.baimicro.central.websocket.pojo.StoreRpcServiceInstance;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @program: hospital-cloud-platform
 * @description: Rpc服务类 服务处理器
 * @author: baiHoo.chen
 * @create: 2020-04-21
 **/
@Slf4j
class RpcServiceServerHandler extends SimpleChannelInboundHandler<RpcServiceRequest> {

    private ObjectMapper objectMapper;

    private PojoEndpointServer pojoEndpointServer;

    private ChannelHandlerContext ctx;

    /**
     * @param pojoEndpointServer
     */
    protected RpcServiceServerHandler(PojoEndpointServer pojoEndpointServer) {
        this.pojoEndpointServer = pojoEndpointServer;
        objectMapper = new ObjectMapper();
    }

    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcServiceRequest msg) {

        System.out.println("rpc server reading client send message:" + msg);
    }

    /**
     * 自定义 通道读取 rpc 调用数据
     *
     * @param pipeline
     * @param msg
     * @throws Exception
     */
    protected void channelRead(ChannelPipeline pipeline, RpcServiceRequest msg) throws Exception {
        Result result;
        if (msg.getServiceName() != null && msg.getMethodName() != null) {
            pipeline.remove("httpServerCodec");
            pipeline.remove("httpObjectAggregator");
            // 添加解码器和编码器，防止拆包和粘包问题
            pipeline.addLast("lengthFieldBasedFrameDecoder",
                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
            //⚠️注意：️ RpcServiceClientHandler 解析字符串，开头会存在 4 个字节预设字节
            pipeline.addLast("lengthFieldPrepender",
                    new LengthFieldPrepender(4));
            pipeline.addLast("stringEncoder",
                    new StringEncoder(CharsetUtil.UTF_8));

            String serviceName = msg.getServiceName();
            String methodName = msg.getMethodName();
            Object object = StoreRpcServiceInstance.get(serviceName);
            Method method = object.getClass().getDeclaredMethod(methodName, msg.getParameterTypes());
            method.setAccessible(true);
            Object res = method.invoke(object, msg.getParameters());
            if (res instanceof Result) {
                result = (Result) res;
            } else {
                result = new Result();
            }
            result.setRequestId(msg.getRequestId());
            pipeline.channel().writeAndFlush(objectMapper.writeValueAsString(result));
        } else {
            result = new Result<>(1, "无法远程调用 RPC 服务方法，原因：可能 beanName 或者 methodName 为空");
            result.setRequestId(msg.getRequestId());
            pipeline.channel().writeAndFlush(objectMapper.writeValueAsString(result));
        }
        System.out.println("rpc server reading client send message:" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        pojoEndpointServer.doOnError(ctx.channel(), cause);
        System.out.println("rpc server 出现错误");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        pojoEndpointServer.doOnEvent(ctx.channel(), evt);
        System.out.println("rpc server 出现异常");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        pojoEndpointServer.doOnClose(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        System.out.println("RpcServiceServerHandler channel 激活");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("rpc server 移除通道");
    }

    /**
     * 自定义控制接收消息
     *
     * @param msg
     * @return
     * @throws Exception
     */
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {

        boolean flag = super.acceptInboundMessage(msg);
        try {
            if (msg instanceof ByteBuf) {
                // 方案 一： 以下解析 RpcServiceRequest 请求数据解析正确
                String str;
                ByteBuf buf = (ByteBuf) msg;
                if (buf.hasArray()) { // 处理堆缓冲区
                    str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
                } else { // 处理直接缓冲区以及复合缓冲区
                    byte[] bytes = new byte[buf.readableBytes()];
                    buf.getBytes(buf.readerIndex(), bytes);
                    str = new String(bytes);
                }
                // ⚠️注意：️解析字符串，开头会存在 4 个字节预设字节
                if (str.length() > 4 && str.contains("requestId")) {
                    str = str.substring(4, str.length());
                    RpcServiceRequest request = objectMapper.readValue(str, RpcServiceRequest.class);
                    if (request != null) {
                        ChannelPipeline pipeline = ctx.pipeline();
                        channelRead(pipeline, request);
                    }
                }
                // 方案 二： 以下解析 RpcServiceRequest 请求数据解析正确，
                /*ByteBuf buf = (ByteBuf) msg;
                // 以下获取 byte[] 字节，还是引发向下连锁异常 【io.netty.handler.codec.http.websocketx.CorruptedWebSocketFrameException: fragmented control frame】并且消息会被拦截
                byte[] bytes = new RpcServiceServerHandler.ByteBufToBytes().read(buf);
                if (bytes.length > 0) {
                    KryoSerializer serializer = new KryoSerializer();
                    RpcServiceRequest request = serializer.deserialize(RpcServiceRequest.class, bytes);
                    if (request != null) {
                        channelRead0(ctx, request);
                    }
                }*/
            }
        } catch (Exception e) {
            log.warn("未解析到 RpcServiceRequest 原因：{}", e);
        }
        return flag;
    }

    static class ByteBufToBytes {
        /**
         * 将ByteBuf转换为byte[]
         *
         * @param byteBuf
         * @return
         */
        byte[] read(ByteBuf byteBuf) {
            //因为之前编码的时候写入一个Int型，4个字节来表示长度
            if (byteBuf.readableBytes() < 4) {
                return null;
            }
            //标记当前读的位置
            byteBuf.markReaderIndex();
            int dataLength = byteBuf.readInt();
            if (byteBuf.readableBytes() < dataLength) {
                byteBuf.resetReaderIndex();
                return null;
            }
            if (dataLength > 0) {
                byte[] bytes = new byte[dataLength];
                //将byteBuf中的数据读入data字节数组
                byteBuf.readBytes(bytes);
                return bytes;
            } else {
                return null;
            }
        }
    }
}
