package com.baimicro.central.websocket.protocol;

import com.baimicro.central.websocket.protocol.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: hospital-cloud-platform
 * @description: Rpc 数据序列化加码
 * @author: baiHoo.chen
 * @create: 2020-04-22
 **/
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> clazz;
    private Serializer serializer;
    private int serialVersion;

    public RpcEncoder(Class<?> clazz, Serializer serializer, int serialVersion) {
        this.clazz = clazz;
        this.serializer = serializer;
        this.serialVersion = serialVersion;
    }


    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        if (clazz != null && clazz.isInstance(msg)) {
            byte[] bytes = serializer.serialize(msg);
            // 自定义协议头
            byteBuf.writeInt(serialVersion);
            byteBuf.writeInt(clazz.getName().getBytes().length);
            byteBuf.writeBytes(clazz.getName().getBytes());

            // 内容主体
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
