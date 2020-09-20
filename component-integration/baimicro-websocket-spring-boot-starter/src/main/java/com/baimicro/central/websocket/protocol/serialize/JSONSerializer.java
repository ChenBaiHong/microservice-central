package com.baimicro.central.websocket.protocol.serialize;

import com.alibaba.fastjson.JSON;

/**
 * @program: hospital-cloud-platform
 * @description: 使用fastJson作为序列化框架
 * @author: baiHoo.chen
 * @create: 2020-04-22
 **/
public class JSONSerializer implements Serializer{

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
