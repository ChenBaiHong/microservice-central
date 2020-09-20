package com.baimicro.central.websocket.future;

import com.baimicro.central.websocket.model.Result;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: hospital-cloud-platform
 * @description: 自定义 实现 future
 * @author: baiHoo.chen
 * @create: 2020-04-23
 **/
public class DefaultFuture {

    private Result result=null;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    // 解决超时，webSocket 等待问题
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * @param timeout 获取超时时间
     * @return
     */
    public Result getResult(int timeout) {
        synchronized (object) {
            while (!isSucceed && atomicInteger.intValue() <= timeout) {
                try {
                    object.wait(timeout);
                    atomicInteger.incrementAndGet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            atomicInteger = new AtomicInteger(0);
            return result;
        }
    }

    public void setResult(Result result) {
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.result = result;
            this.isSucceed = true;
            object.notify();
        }
    }
}
