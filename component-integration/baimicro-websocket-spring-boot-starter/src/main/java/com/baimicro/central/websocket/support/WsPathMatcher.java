package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 访问路径匹配
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public interface WsPathMatcher {

    String getPattern();

    boolean matchAndExtract(QueryStringDecoder decoder, Channel channel);
}
