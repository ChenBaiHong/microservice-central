package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.core.MethodParameter;

/**
 * @program: hospital-cloud-platform
 * @description: HttpHeader 头 参数处理解析类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class HttpHeadersMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return HttpHeaders.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        if (object != null) {
            return ((FullHttpRequest) object).headers();
        }
        return null;
    }
}
