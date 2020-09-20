package com.baimicro.central.websocket.protocol.serialize;

import java.io.IOException;

/**
 * @program: hospital-cloud-platform
 * @description: 序列化接口
 * @author: baiHoo.chen
 * @create: 2020-04-22
 **/
public interface Serializer {
    /**
     * java对象转换为二进制
     *
     * @param object
     * @return
     */
    byte[] serialize(Object object) throws IOException;

    /**
     * 二进制转换成java对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;
}
