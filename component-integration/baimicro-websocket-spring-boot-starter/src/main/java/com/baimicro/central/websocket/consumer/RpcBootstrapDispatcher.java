package com.baimicro.central.websocket.consumer;

import com.baimicro.central.websocket.future.DefaultFuture;
import com.baimicro.central.websocket.model.Result;
import com.baimicro.central.websocket.model.RpcServiceRequest;
import com.baimicro.central.websocket.protocol.RpcDecoder;
import com.baimicro.central.websocket.protocol.RpcEncoder;
import com.baimicro.central.websocket.protocol.serialize.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: hospital-cloud-platform
 * @description: Rpc 消费服务启动调用
 * @author: baiHoo.chen
 * @create: 2020-04-21
 **/
@Slf4j
@ChannelHandler.Sharable
public class RpcBootstrapDispatcher {

    /**
     * 使用 Map 维护已经启动 RPC 服务，IP 和 port 做为主键
     */
    private final static Map<String, RpcBootstrapDispatcher> rpcBootstrapDispatcherMap = new ConcurrentHashMap<>();

    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private RpcServiceClientHandler clientHandler;
    private String host;
    private Integer port;
    private static final int MAX_RETRY = 5;


    /**
     * 同样的  RPC 服务 请求 IP 和 port 不需要重复实例化出一个个客户端
     *
     * @param host
     * @param port
     * @return
     */
    public static RpcBootstrapDispatcher INSTANCE(String host, Integer port) {
        RpcBootstrapDispatcher rpcBootstrapDispatcher = rpcBootstrapDispatcherMap.get(host + "_" + port);
        if (rpcBootstrapDispatcher == null) {
            rpcBootstrapDispatcher = new RpcBootstrapDispatcher(host, port);
            rpcBootstrapDispatcherMap.putIfAbsent(host + "_" + port, rpcBootstrapDispatcher);
        }
        return rpcBootstrapDispatcher;
    }

    private RpcBootstrapDispatcher() {
    }

    private RpcBootstrapDispatcher(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        clientHandler = new RpcServiceClientHandler();
        eventLoopGroup = new NioEventLoopGroup();
        //启动类
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //指定传输使用的Channel
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcServiceRequest.class, new KryoSerializer(), 1000));
                        pipeline.addLast(new RpcDecoder<>(Result.class, new KryoSerializer(), 1000));
                        pipeline.addLast(clientHandler);
                    }
                });
        connect(bootstrap, host, port, MAX_RETRY);
    }

    /**
     * 失败重连机制
     *
     * @param bootstrap
     * @param host      地址
     * @param port      端口
     * @param retry     重试次数
     */
    private void connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("连接服务端成功");
            } else if (retry == 0) {
                log.error("重试次数已用完，放弃连接");
            } else {
                //第几次重连：
                int order = (MAX_RETRY - retry) + 1;
                //本次重连的间隔
                int delay = 1 << order;
                log.error("{} : 连接失败，第 {} 重连....", new Date(), order);
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
        channel = channelFuture.channel();
    }

    /**
     * 创建一个代理对象
     */
    public Object getBean(Class<?> serviceClass, String beanName, String methodName) {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    if (method.getName().equals(methodName)) {
                        if (clientHandler == null) {
                            connect();
                        }
                        List<Class> list = new LinkedList<>();
                        for (Object arg : args) {
                            list.add(arg.getClass());
                        }
                        RpcServiceRequest request = new RpcServiceRequest(beanName, methodName, list.toArray(new Class[args.length]), args);
                        request.setRequestId(UUID.randomUUID().toString());
                        //发送请求对象之前，先把请求ID保存下来，并构建一个与响应Future的映射关系
                        clientHandler.futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
                        return send(request);
                    } else {
                        return method.invoke(proxy, args);
                    }
                });
    }

    /**
     * 发送消息
     *
     * @param request
     * @return
     */
    public Result send(final RpcServiceRequest request) {
        try {
            channel.writeAndFlush(request).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientHandler.getResult(request.getRequestId());
    }

    @PreDestroy
    public void close() {
        rpcBootstrapDispatcherMap.remove(this.host + "_" + this.port);
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}
