package com.baimicro.central.websocket.support;

import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

/**
 * @program: hospital-cloud-platform
 * @description: 方法参数解析类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/

public interface MethodArgumentResolver {

    /**
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by this resolver.
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    boolean supportsParameter(MethodParameter parameter);


    @Nullable
    Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception;

}
