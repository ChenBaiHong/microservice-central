package com.baimicro.central.websocket.support;

import com.baimicro.central.websocket.annotation.OnError;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

/**
 * @program: hospital-cloud-platform
 * @description: 若使用 @OnError 注解，当有 websocket 抛出异常时，解析处理类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class ThrowableMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethod().isAnnotationPresent(OnError.class) && Throwable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        if (object instanceof Throwable) {
            return object;
        }
        return null;
    }
}

