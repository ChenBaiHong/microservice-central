package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 默认 处理路径
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class DefaultPathMatcher implements WsPathMatcher {

    private String pattern;

    public DefaultPathMatcher(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public boolean matchAndExtract(QueryStringDecoder decoder, Channel channel) {
        if (!pattern.equals(decoder.path())) {
            return false;
        }
        return true;
    }
}
