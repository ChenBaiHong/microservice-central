scada-socket-spring-boot-starter [![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
===================================

### 简介
本项目为电力信息采集通信主站部分，是在 spring-boot 中使用 Netty 来开发 WebSocket 服务器，并像 spring-websocket 的注解开发一样简单，实现 WebSocket 的高可用，持服务内部 RPC 调用，适合分布式实时通信场景。支持 TCP 通信，但只适用终端通信场景，进行信息采集接收，如终端登录帧，主站命令帧，响应帧，确认帧。


### 要求
- jdk版本为1.8或1.8+

### 快速开始

- 添加 maven 依赖:
```xml
<dependency>
    <groupId>com.cdhld.scada</groupId>
    <artifactId>scada-socket-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
- 添加 gradle 依赖: 
```groovy
implementation 'com.cdhld.scada:scada-socket-spring-boot-starter:1.0.0'
```
- 在端点类上加上`@ServerEndpoint`注解，并在相应的方法上加上`@BeforeHandshake`、`@OnOpen`、`@OnClose`、`@OnError`、`@OnMessage`、`@OnMessageFrame`、`@OnBinary`、`@OnEvent`注解，样例如下：
```java
//@ServerEndpoint
@ServerEndpoint("/scada")
@Component
@Slf4j
public class SocketServerEndpoint {

    @Value("${server.port}")
    private Integer port;

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    /**
     * 建立连接之前获取 token 认证
     *
     * @param httpRequest
     * @param httpResponse
     */
    @BeforeHandshake
    public FullHttpResponse beforeHandshake(FullHttpRequest httpRequest, FullHttpResponse httpResponse) {
        httpResponse.setStatus(HttpResponseStatus.OK);
        return httpResponse;
    }

    /**
     * 开启 netty 通信通道
     *
     * @param session
     * @param headers
     * @throws IOException
     * @throws InterruptedException
     */
    @OnOpen
    public synchronized void onOpen(Session session, HttpHeaders headers) throws IOException, InterruptedException {
        channelGroup.add(session.channel());
        System.out.println("开启隧道连接: " + session.channel().id().asLongText());
    }


    /**
     * 处理消息
     *
     * @param session
     * @param text
     */

    @OnMessage
    public void OnMessage(Session session, String text) throws Exception {
        System.out.println(text);
        IMessageService messageService;
        // 测试 服务之间 RPC 调用
        if (port == 29011) {
            messageService = (IMessageService) RpcBootstrapDispatcher.INSTANCE(
                    "192.168.0.12", 29012)
                    .getBean(IMessageService.class, "messageService", "sendMessage");
        } else {
            messageService = (IMessageService) RpcBootstrapDispatcher.INSTANCE(
                    "192.168.0.12", 29011)
                    .getBean(IMessageService.class, "messageService", "sendMessage");
        }
        if(messageService!=null){
            Result<?> result = messageService.sendMessage("", text);
            System.out.println(result.getResult());
        }
    }

    /**
     * 处理终端报文帧
     *
     * @param session
     * @param requestFrame
     * @throws Exception
     */
    @OnMessageFrame
    public void OnMessageFrame(Session session, RequestFrame requestFrame) throws Exception {
        System.out.println(requestFrame.getAnalysisContent());
        if (requestFrame.getPacketFrame().getApplicationLayer().getFrameSeqDomain().getCON() == 1) {
            AssemblePacket assemblePacket = new AssemblePacket(requestFrame.getPacketFrame());
            PacketFrame packetFrame = new PacketFrame();
            packetFrame.setAddressDomain(requestFrame.getPacketFrame().getAddressDomain());
            packetFrame.setControlDomain(new ControlDomain(0, 0, 0, 0, 9));
            packetFrame.setApplicationLayer(new ApplicationLayer(new FrameSeqDomain(0, 1, 1, 0)));
            DecimalTools myTools = new DecimalTools();
            myTools.writeToClient(assemblePacket.buildResponseFrame(packetFrame).getAssemblePacket().toString(), session.getChannelHandlerContext(), "确认针");

        }
    }

    /**
     * 关闭连接
     *
     * @param session
     * @throws IOException
     */
    @OnClose
    public synchronized void onClose(Session session) throws Exception {
        System.out.println("关闭隧道连接: " + session.channel().id().asLongText());
        channelGroup.remove(session.channel());
    }

    /**
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        log.info("通信出现错误: " + throwable);
    }
}
```
- 打开WebSocket客户端，连接到`ws://127.0.0.1:80/scada`
### 注解
###### @ServerEndpoint 
> 当ServerEndpointExporter类通过Spring配置进行声明并被使用，它将会去扫描带有@ServerEndpoint注解的类
> 被注解的类将被注册成为一个WebSocket端点
> 所有的[配置项](#%E9%85%8D%E7%BD%AE)都在这个注解的属性中 ( 如:`@ServerEndpoint("/ws")` )

###### @BeforeHandshake 
> 当有新的连接进入时，对该方法进行回调
> 注入参数的类型:Session、HttpHeaders...

###### @OnOpen 
> 当有新的WebSocket连接完成时，对该方法进行回调
> 注入参数的类型:Session、HttpHeaders...

###### @OnClose
> 当有WebSocket连接关闭时，对该方法进行回调
> 注入参数的类型:Session

###### @OnError
> 当有WebSocket抛出异常时，对该方法进行回调
> 注入参数的类型:Session、Throwable

###### @OnMessage
> 当接收到字符串消息时，对该方法进行回调
> 注入参数的类型:Session、String
>
###### @OnMessageFrame
> 当接收到终端设备请求的报文，
> 注入参数的类型:Session、RequestFrame
>
###### @OnBinary
> 当接收到二进制消息时，对该方法进行回调
> 注入参数的类型:Session、byte[]

###### @OnEvent
> 当接收到Netty的事件时，对该方法进行回调
> 注入参数的类型:Session、Object

### 配置
> 所有的配置项都在这个注解的属性中

| 属性  | 默认值 | 说明 
|---|---|---
|path|"/"|WebSocket的path,也可以用`value`来设置
|host|"0.0.0.0"|WebSocket的host,`"0.0.0.0"`即是所有本地地址
|port|80|WebSocket绑定端口号。如果为0，则使用随机端口(端口获取可见 [多端点服务](#%E5%A4%9A%E7%AB%AF%E7%82%B9%E6%9C%8D%E5%8A%A1))
|bossLoopGroupThreads|0|bossEventLoopGroup的线程数
|workerLoopGroupThreads|0|workerEventLoopGroup的线程数
|useCompressionHandler|false|是否添加WebSocketServerCompressionHandler到pipeline
|optionConnectTimeoutMillis|30000|与Netty的`ChannelOption.CONNECT_TIMEOUT_MILLIS`一致
|optionSoBacklog|128|与Netty的`ChannelOption.SO_BACKLOG`一致
|childOptionWriteSpinCount|16|与Netty的`ChannelOption.WRITE_SPIN_COUNT`一致
|childOptionWriteBufferHighWaterMark|64*1024|与Netty的`ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK`一致,但实际上是使用`ChannelOption.WRITE_BUFFER_WATER_MARK`
|childOptionWriteBufferLowWaterMark|32*1024|与Netty的`ChannelOption.WRITE_BUFFER_LOW_WATER_MARK`一致,但实际上是使用 `ChannelOption.WRITE_BUFFER_WATER_MARK`
|childOptionSoRcvbuf|-1(即未设置)|与Netty的`ChannelOption.SO_RCVBUF`一致
|childOptionSoSndbuf|-1(即未设置)|与Netty的`ChannelOption.SO_SNDBUF`一致
|childOptionTcpNodelay|true|与Netty的`ChannelOption.TCP_NODELAY`一致
|childOptionSoKeepalive|false|与Netty的`ChannelOption.SO_KEEPALIVE`一致
|childOptionSoLinger|-1|与Netty的`ChannelOption.SO_LINGER`一致
|childOptionAllowHalfClosure|false|与Netty的`ChannelOption.ALLOW_HALF_CLOSURE`一致
|readerIdleTimeSeconds|0|与`IdleStateHandler`中的`readerIdleTimeSeconds`一致，并且当它不为0时，将在`pipeline`中添加`IdleStateHandler`
|writerIdleTimeSeconds|0|与`IdleStateHandler`中的`writerIdleTimeSeconds`一致，并且当它不为0时，将在`pipeline`中添加`IdleStateHandler`
|allIdleTimeSeconds|0|与`IdleStateHandler`中的`allIdleTimeSeconds`一致，并且当它不为0时，将在`pipeline`中添加`IdleStateHandler`
|maxFramePayloadLength|65536|最大允许帧载荷长度

### 通过application.properties进行配置
> 所有参数皆可使用`${...}`占位符获取`application.properties`中的配置。如下：

- 首先在`@ServerEndpoint`注解的属性中使用`${...}`占位符
```java
@ServerEndpoint(host = "${ws.host}",port = "${ws.port}")
public class MyWebSocket {
    ...
}
```
- 接下来即可在`application.properties`中配置
```
ws.host=0.0.0.0
ws.port=80
```

### 自定义Favicon
配置favicon的方式与spring-boot中完全一致。只需将`favicon.ico`文件放到classpath的根目录下即可。如下:
```
src/
  +- main/
    +- java/
    |   + <source code>
    +- resources/
        +- favicon.ico
```

### 自定义错误页面
配置自定义错误页面的方式与spring-boot中完全一致。你可以添加一个 `/public/error` 目录，错误页面将会是该目录下的静态页面，错误页面的文件名必须是准确的错误状态或者是一串掩码,如下：
```
src/
  +- main/
    +- java/
    |   + <source code>
    +- resources/
        +- public/
            +- error/
            |   +- 404.html
            |   +- 5xx.html
            +- <other public assets>
```

### 多端点服务
- 在[快速启动](#%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B)的基础上，在多个需要成为端点的类上使用`@ServerEndpoint`、`@Component`注解即可
- 可通过`ServerEndpointExporter.getInetSocketAddressSet()`获取所有端点的地址
- 当地址不同时(即host不同或port不同)，使用不同的`ServerBootstrap`实例
- 当地址相同,路径(path)不同时,使用同一个`ServerBootstrap`实例
- 当多个端点服务的port为0时，将使用同一个随机的端口号
- 当多个端点的port和path相同时，host不能设为`"0.0.0.0"`，因为`"0.0.0.0"`意味着绑定所有的host
