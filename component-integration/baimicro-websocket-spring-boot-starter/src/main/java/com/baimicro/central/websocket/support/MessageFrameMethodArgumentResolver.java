package com.baimicro.central.websocket.support;

import com.baimicro.central.websocket.annotation.OnMessageFrame;
import com.baimicro.central.websocket.model.RequestFrame;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

/**
 * @project: hualida-scada
 * @author: chen.baihoo
 * @date: 2020/9/2
 * @Description: TODO
 * version 0.1
 */
public class MessageFrameMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethod().isAnnotationPresent(OnMessageFrame.class) && RequestFrame.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        return object;
    }
}
