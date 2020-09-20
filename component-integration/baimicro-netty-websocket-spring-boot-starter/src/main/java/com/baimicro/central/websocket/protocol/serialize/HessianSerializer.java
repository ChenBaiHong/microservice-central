package com.baimicro.central.websocket.protocol.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @program: hospital-cloud-platform
 * @description: Hessian 序列化方案经常被 RPC 框架用来作为默认的序列化方案,比如dubbo，motan
 * @author: baiHoo.chen
 * @create: 2020-04-22
 **/
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(byteArrayOutputStream);
        output.writeObject(object);
        output.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        return (T) input.readObject(clazz);
    }
}
