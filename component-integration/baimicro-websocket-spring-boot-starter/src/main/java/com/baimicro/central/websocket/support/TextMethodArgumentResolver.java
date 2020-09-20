package com.baimicro.central.websocket.support;

import com.baimicro.central.websocket.annotation.OnMessage;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.core.MethodParameter;

/**
 * @program: hospital-cloud-platform
 * @description: 若使用 @OnMessage注解，收到用户消息解析处理类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class TextMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethod().isAnnotationPresent(OnMessage.class) && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        TextWebSocketFrame textFrame = (TextWebSocketFrame) object;
        return textFrame.text();
    }
}
