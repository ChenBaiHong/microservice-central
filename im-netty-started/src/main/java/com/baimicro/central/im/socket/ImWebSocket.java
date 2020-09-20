package com.baimicro.central.im.socket;

import com.baimicro.central.im.constant.IMServerConstant;
import com.baimicro.central.im.enums.ChatEnum;
import com.baimicro.central.im.model.Message;
import com.baimicro.central.im.model.SessionGroup;
import com.baimicro.central.im.model.UsedInfo;
import com.baimicro.central.im.rpcservice.IMessageService;
import com.baimicro.central.redis.template.RedisRepository;
import com.baimicro.central.websocket.annotation.*;
import com.baimicro.central.websocket.consumer.RpcBootstrapDispatcher;
import com.baimicro.central.websocket.model.Result;
import com.baimicro.central.websocket.pojo.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 服务启动服务
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@ServerEndpoint
@Component
@Slf4j
@SuppressWarnings("unchecked")
public class ImWebSocket {


//    @Autowired
//    private OAuth2TokenUtil oAuth2TokenUtil;

    @Autowired
    private RedisRepository redisRepository;

    @Value("${his.cloud.netty.websocket.host}")
    private String wsHost;
    @Value("${server.port}")
    private String port;

    private HashSet<String> wsColony;

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    /**
     * 存储 websocket 集群 地址端口
     */
    @PostConstruct
    public void initialize() {
        wsColony = (HashSet<String>)
                redisRepository.get(IMServerConstant.REDIS_WEBSOCKET_COLONY);
        if (wsColony == null) {
            wsColony = new HashSet<>();
            wsColony.add(IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port);
        } else {
            wsColony.add(IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port);
        }
        redisRepository.set(IMServerConstant.REDIS_WEBSOCKET_COLONY, wsColony);

    }

    /**
     * 建立连接之前获取 token 认证
     *
     * @param httpRequest
     * @param httpResponse
     */
    @BeforeHandshake
    public FullHttpResponse beforeHandshake(FullHttpRequest httpRequest, FullHttpResponse httpResponse) {
        try {

            /**
             * websocket 集群下 ，鉴权认证通过 getway 就可以了
             *
             *
             OAuth2Authentication auth2Authentication = oAuth2TokenUtil.readAuthentication(
             httpRequest.headers().get("Authorization"));
             if (auth2Authentication.getPrincipal() == null) {
             httpResponse.setStatus(HttpResponseStatus.UNAUTHORIZED);
             }*/
            httpResponse.setStatus(HttpResponseStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通信握手时获取用户身份信息异常：{}", e.getMessage());
            httpResponse.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);

        }
        return httpResponse;
    }

    /**
     * 开启 nettt 通信通道，保存获取识别ID
     *
     * @param imUserId
     * @param session
     * @param headers
     * @throws IOException
     * @throws InterruptedException
     */
    @OnOpen
    public synchronized void onOpen(@RequestParam("imUserId") String imUserId, Session session, HttpHeaders headers) throws IOException, InterruptedException {
        String clientId = headers.get("clientId");
        if (!checkStringValue(clientId, imUserId)) {
            session.channel().closeFuture().sync();
            log.debug("imUserId 或者 clientId 不存在，不能建立连接");
            return;
        }

        String sessionKey = session.id().asLongText();
        redisRepository.set(
                IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port + ":" + sessionKey,
                new SessionGroup(imUserId, clientId, sessionKey, wsHost, port));

        sessionKey = clientId + "_" + imUserId;
        redisRepository.set(
                IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port + ":" + sessionKey,
                new SessionGroup(imUserId, clientId, sessionKey, wsHost, port));

        // 全局添加 channel
        channelGroup.add(session.channel());
        log.info("加入一个新的连接");
    }

    /**
     * 关闭连接
     *
     * @param session
     * @throws IOException
     */
    @OnClose
    public synchronized void onClose(Session session) throws Exception {
        SessionGroup sessionGroup = (SessionGroup) redisRepository.get(
                IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port + ":" + session.id().asLongText());
        if (sessionGroup != null) {
            redisRepository.del(
                    IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port + ":"
                            + session.id().asLongText()
                    , IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port + ":"
                            + sessionGroup.getClientId() + "_" + sessionGroup.getImUserId()
            );
        }
        //channelGroup.remove(session.channel());
        log.info("移除一个的连接");
    }

    /**
     * 处理洗消息
     *
     * @param session
     * @param text
     */

    @OnMessage
    public void OnMessage(Session session, String text) throws Exception {
        if (checkStringValue(text)) {
            log.info("发送一个消息：{}", text);

            String sessionKey;
            String channelId;
            SessionGroup sessionGroup;
            // 1. 定义映射对象字节解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 2. 读取字符串文本解析成对象
            UsedInfo usedInfo = objectMapper.readValue(text, UsedInfo.class);
            Message message = usedInfo.getMessage();
            if (message == null) {
                log.info("发送消息的消息体是空的？");
                return;
            }
            // 3. 在线消息
            if (ChatEnum.MSG_ONLINE.getDictCode().equals(usedInfo.getCode())) {
                // 3.1 单聊
                if (ChatEnum.MSG_SINGLE.getDictCode().equals(message.getChatType())) {
                    // 发送给对方的 识别 回话key
                    sessionKey = message.getToClientId() + "_" + message.getToId();

                    sessionGroup = (SessionGroup) redisRepository.get(
                            IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port + ":" + sessionKey);

                    //1. 对方在当前服务器
                    if (sessionGroup != null) {
                        channelId = sessionGroup.getChannelId();
                        Optional<Channel> channelOptional = channelGroup.stream().filter(
                                e -> e.id().asLongText().equals(channelId))
                                .findFirst();
                        // 1.1 对方在线，发给对方
                        if (channelOptional != null && channelOptional.isPresent()) {
                            channelOptional.get().writeAndFlush(text);
                            return;
                        }
                    }
                    // 2. 判断对方是否在其他服务器
                    else {
                        wsColony = (HashSet<String>)
                                redisRepository.get(IMServerConstant.REDIS_WEBSOCKET_COLONY);
                        if (wsColony != null) {
                            List<String> list = wsColony.stream().filter(e ->
                                    !e.equals(IMServerConstant.REDIS_WEBSOCKET_COLONY_NODENAME + wsHost + "_" + port)).collect(Collectors.toList());
                            for (String nodeName : list) {
                                sessionGroup = (SessionGroup) redisRepository.get(nodeName + ":" + sessionKey);
                                if (sessionGroup != null) {
                                    IMessageService messageService = (IMessageService) RpcBootstrapDispatcher.INSTANCE(sessionGroup.getHost(), Integer.parseInt(sessionGroup.getPort()))
                                            .getBean(IMessageService.class, "messageService", "sendMessage");
                                    Result result = messageService.sendMessage(sessionGroup.getClientId(), text);
                                    if (result != null && (result.getCode() == 0 || result.getCode() == 200))
                                        return;
                                }
                            }

                        }
                    }
                }
                // 3.2 群聊
                else {


                }
            }
            usedInfo = new UsedInfo(ChatEnum.MSG_OFFLINE.getDictCode());
            session.sendText(objectMapper.writeValueAsString(usedInfo));
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {

        throwable.printStackTrace();
        log.info("通信出现错误：{}", throwable);
    }

    private boolean checkStringValue(String... value) {
        if (value != null)
            for (String e :
                    value) {
                if (e == null || "undefined".equals(e) || "null".equals(e)) {
                    return false;
                }
            }
        return true;
    }
}
