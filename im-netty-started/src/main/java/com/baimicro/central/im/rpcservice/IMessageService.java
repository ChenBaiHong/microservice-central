package com.baimicro.central.im.rpcservice;

import com.baimicro.central.websocket.model.Result;

/**
 * @program: hospital-cloud-platform
 * @description: 消息服务类接口
 * @author: baiHoo.chen
 * @create: 2020-04-21
 **/
public interface IMessageService {

    public Result sendMessage(String channelId, String text);
}
