package com.baimicro.central.websocket.standard;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @program: hospital-cloud-platform
 * @description: 端点服务常规化配置
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Getter
@Setter
@ConfigurationProperties("his.cloud.netty.websocket")
public class ServerEndpointConfig {

    private String path = "/";

    private String host;

    /**
     * 9326 为 websocket 默认启动端口
     */
    private Integer port;

    private Integer bossLoopGroupThreads = 0;

    private Integer workerLoopGroupThreads = 0;

    private Boolean useCompressionHandler = false;

    private String[] subprotocols;

    //------------------------- option -------------------------

    private Integer connectTimeoutMillis = 30000;

    private Integer soBacklog = 128;

    //------------------------- childOption -------------------------

    private Integer writeSpinCount = 16;

    private Integer writeBufferHighWaterMark = 65536;

    private Integer writeBufferLowWaterMark = 32768;

    private Integer soRcvbuf = -1;

    private Integer soSndbuf = -1;

    private Boolean tcpNodelay = true;

    private Boolean soKeepalive = false;

    private Integer soLinger = -1;

    private Boolean allowHalfClosure = false;

    //------------------------- idleEvent -------------------------

    private Integer readerIdleTimeSeconds = 0;

    private Integer writerIdleTimeSeconds = 0;

    private Integer allIdleTimeSeconds = 0;

    //------------------------- handshake -------------------------

    private Integer maxFramePayloadLength = 65536;

    private static Integer randomPort;

    private Integer getAvailablePort(Integer port) {
        if (port != 0) {
            return port;
        }
        if (randomPort != null && randomPort != 0) {
            return randomPort;
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
        Socket socket = new Socket();
        try {
            socket.bind(inetSocketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int localPort = socket.getLocalPort();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        randomPort = localPort;
        return localPort;
    }
}

