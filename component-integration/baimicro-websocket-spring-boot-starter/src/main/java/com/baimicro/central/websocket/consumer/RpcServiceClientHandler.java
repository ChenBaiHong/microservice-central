package com.baimicro.central.websocket.consumer;

import com.baimicro.central.websocket.future.DefaultFuture;
import com.baimicro.central.websocket.model.Result;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: hospital-cloud-platform
 * @description: rpc 服务 客户端处理操作
 * @author: baiHoo.chen
 * @create: 2020-04-23
 **/
@Slf4j
public class RpcServiceClientHandler extends ChannelDuplexHandler {

    /**
     * 使用Map维护请求对象ID与响应结果Future的映射关系
     */
    protected final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) throws Exception {
        if (msgObj instanceof Result) {
            Result<?> result = (Result<?>) msgObj;
            //获取响应对象
            try {
                DefaultFuture defaultFuture = futureMap.get(result.getRequestId());
                defaultFuture.setResult(result);
            } catch (Exception e) {
                log.warn("未解析到 Result 原因: " + e);
            }
        }
        super.channelRead(ctx, msgObj);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    /**
     * 获取响应结果
     *
     * @param requestId
     * @return
     */
    public Result getResult(String requestId) {
        try {
            DefaultFuture future = futureMap.get(requestId);
            return future.getResult(3);
        } finally {
            //获取成功以后，从map中移除
            futureMap.remove(requestId);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("RpcServiceClientHandler channel 激活");
    }
}
