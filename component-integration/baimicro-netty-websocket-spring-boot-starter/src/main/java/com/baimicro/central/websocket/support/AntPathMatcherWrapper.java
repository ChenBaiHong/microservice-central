package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.springframework.util.AntPathMatcher;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.baimicro.central.websocket.pojo.PojoEndpointServer.URI_TEMPLATE;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 处理访问路径 包装器类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class AntPathMatcherWrapper extends AntPathMatcher implements WsPathMatcher {

    private String pattern;

    public AntPathMatcherWrapper(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }

    /**
     * @param decoder
     * @param channel
     * @return
     */
    @Override
    public boolean matchAndExtract(QueryStringDecoder decoder, Channel channel) {
        Map<String, String> variables = new LinkedHashMap<>();
        boolean result = doMatch(pattern, decoder.path(), true, variables);
        if (result) {
            channel.attr(URI_TEMPLATE).set(variables);
            return true;
        }
        return false;
    }
}
