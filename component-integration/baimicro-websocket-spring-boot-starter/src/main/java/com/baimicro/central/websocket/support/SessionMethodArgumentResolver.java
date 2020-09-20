package com.baimicro.central.websocket.support;

import com.baimicro.central.websocket.pojo.Session;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

import static com.baimicro.central.websocket.pojo.PojoEndpointServer.SESSION_KEY;

/**
 * @program: hospital-cloud-platform
 * @description: websocket 建立一个连接就会生成一个 session 会话， 该类将对 session 处理解析
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class SessionMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Session.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        Session session = channel.attr(SESSION_KEY).get();
        return session;
    }
}
