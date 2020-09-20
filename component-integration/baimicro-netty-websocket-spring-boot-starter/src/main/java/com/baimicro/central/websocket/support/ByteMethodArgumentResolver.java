package com.baimicro.central.websocket.support;

import com.baimicro.central.websocket.annotation.OnBinary;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.core.MethodParameter;

/**
 * @program: hospital-cloud-platform
 * @description: 若使用 @OnBinary 注解，字节参数处理解析类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class ByteMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethod().isAnnotationPresent(OnBinary.class) && byte[].class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) object;
        ByteBuf content = binaryWebSocketFrame.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        return bytes;
    }
}
