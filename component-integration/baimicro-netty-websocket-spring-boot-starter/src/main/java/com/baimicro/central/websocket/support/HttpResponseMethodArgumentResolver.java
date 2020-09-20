package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.core.MethodParameter;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @program: hospital-cloud-platform
 * @description: 方法存在 HttpResponse 类解析
 * @author: baiHoo.chen
 * @create: 2020-04-13
 **/
public class HttpResponseMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return FullHttpResponse.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {

        return new DefaultFullHttpResponse(HTTP_1_1, OK);
    }
}
