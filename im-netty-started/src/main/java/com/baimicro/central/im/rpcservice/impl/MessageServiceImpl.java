package com.baimicro.central.im.rpcservice.impl;

import com.baimicro.central.im.rpcservice.IMessageService;
import com.baimicro.central.im.socket.ImWebSocket;
import com.baimicro.central.websocket.annotation.RpcService;
import com.baimicro.central.websocket.model.Result;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @program: hospital-cloud-platform
 * @description: 消息处理控制器
 * @author: baiHoo.chen
 * @create: 2020-04-19
 **/
@Slf4j
@RpcService("messageService")
public class MessageServiceImpl implements IMessageService {


    /**
     *
     * @param channelId
     * @param text
     * @return
     */
    public Result sendMessage(String channelId, String text) {
        ChannelGroup channelGroup = ImWebSocket.getChannelGroup();
        Optional<Channel> channelOptional = channelGroup.stream().filter(
                e -> e.id().asLongText().equals(channelId))
                .findFirst();
        Result result = new Result();
        if (channelOptional != null && channelOptional.isPresent())
            channelOptional.get().writeAndFlush(text);
        else
            result.setCode(1);
        return result;
    }
}
