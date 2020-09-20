package com.baimicro.central.websocket.standard;

import com.baimicro.central.websocket.pojo.PojoEndpointServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 服务类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class WebsocketServer {

    private final PojoEndpointServer pojoEndpointServer;

    private final ServerEndpointConfig config;

    public WebsocketServer(PojoEndpointServer pojoEndpointServer, ServerEndpointConfig serverEndpointConfig) {
        this.pojoEndpointServer = pojoEndpointServer;
        this.config = serverEndpointConfig;
    }

    /**
     * 执行顺序步骤 8: 初始启动服务网络
     *
     * @throws InterruptedException
     */
    public void init() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(config.getBossLoopGroupThreads());
        EventLoopGroup worker = new NioEventLoopGroup(config.getWorkerLoopGroupThreads());
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeoutMillis())
                .option(ChannelOption.SO_BACKLOG, config.getSoBacklog())
                .childOption(ChannelOption.WRITE_SPIN_COUNT, config.getWriteSpinCount())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(config.getWriteBufferLowWaterMark(), config.getWriteBufferHighWaterMark()))
                .childOption(ChannelOption.TCP_NODELAY, config.getTcpNodelay())
                .childOption(ChannelOption.SO_KEEPALIVE, config.getSoKeepalive())
                .childOption(ChannelOption.SO_LINGER, config.getSoLinger())
                .childOption(ChannelOption.ALLOW_HALF_CLOSURE, config.getAllowHalfClosure())
                /*.childOption(ChannelOption.AUTO_READ, true)*/
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("tcpServerHandler", new TcpServerHandler(pojoEndpointServer));
                        /**
                         * 添加关于 http 请求的处理器
                         */
                        pipeline.addLast("httpServerCodec", new HttpServerCodec());
                        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
                        pipeline.addLast("httpServerHandler", new HttpServerHandler(pojoEndpointServer, config));
                    }
                });

        if (config.getSoRcvbuf() != -1) {
            bootstrap.childOption(ChannelOption.SO_RCVBUF, config.getSoRcvbuf());
        }

        if (config.getSoSndbuf() != -1) {
            bootstrap.childOption(ChannelOption.SO_SNDBUF, config.getSoSndbuf());
        }

        ChannelFuture channelFuture;
        if ("0.0.0.0".equals(config.getHost())) {
            channelFuture = bootstrap.bind(config.getPort());
        } else {
            try {
                channelFuture = bootstrap.bind(new InetSocketAddress(InetAddress.getByName(config.getHost()), config.getPort()));
            } catch (UnknownHostException e) {
                channelFuture = bootstrap.bind(config.getHost(), config.getPort());
                e.printStackTrace();
            }
        }

        channelFuture.addListener(future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            boss.shutdownGracefully().syncUninterruptibly();
            worker.shutdownGracefully().syncUninterruptibly();
        }));
    }

    public PojoEndpointServer getPojoEndpointServer() {
        return pojoEndpointServer;
    }
}
