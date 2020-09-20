package com.baimicro.central.websocket.protocol;

import com.baimicro.central.websocket.protocol.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @program: hospital-cloud-platform
 * @description: Rpc 数据序列化解码
 * @author: baiHoo.chen
 * @create: 2020-04-22
 **/
public class RpcDecoder<T> extends ByteToMessageDecoder {

    private Class<T> clazz;
    private Serializer serializer;
    private int serialVersion;

    public RpcDecoder(Class<T> clazz, Serializer serializer, int serialVersion) {
        this.clazz = clazz;
        this.serializer = serializer;
        this.serialVersion = serialVersion;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 自定义协议头
        int version = byteBuf.readInt();
        if (serialVersion == version) {
            byte[] data = new byte[byteBuf.readInt()];
            byteBuf.readBytes(data);
            if (clazz.getName().equals(new String(data))) {
                int dataLength = byteBuf.readInt();
                // 内容主体
                data = new byte[dataLength];
                byteBuf.readBytes(data);
                Object obj = serializer.deserialize(clazz, data);
                list.add(obj);
            }
        }
    }

    public T decode(ByteBuf byteBuf) throws Exception {
        // 自定义协议头
        int version = byteBuf.getInt(0);
        if (serialVersion == version) {
            byteBuf.readInt();
            byte[] data = new byte[byteBuf.readInt()];
            byteBuf.readBytes(data);
            if (clazz.getName().equals(new String(data))) {
                int dataLength = byteBuf.readInt();
                // 内容主体
                data = new byte[dataLength];
                byteBuf.readBytes(data);
                return serializer.deserialize(clazz, data);
            }
        }
        return null;
    }
}
