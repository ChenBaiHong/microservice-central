package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.core.MethodParameter;

/**
 * @program: hospital-cloud-platform
 * @description: 方法存在 HttpRequest 类解析
 * @author: baiHoo.chen
 * @create: 2020-04-13
 **/
public class HttpRequestMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return FullHttpRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {

        return object;
    }
}
