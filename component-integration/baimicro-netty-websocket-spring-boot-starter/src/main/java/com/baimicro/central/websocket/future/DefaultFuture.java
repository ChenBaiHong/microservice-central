package com.baimicro.central.websocket.future;

import com.baimicro.central.websocket.model.Result;

/**
 * @program: hospital-cloud-platform
 * @description: 自定义 实现 future
 * @author: baiHoo.chen
 * @create: 2020-04-23
 **/
public class DefaultFuture {

    private Result result;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    /**
     * @param timeout 获取超时时间
     * @return
     */
    public Result getResult(int timeout) {
        synchronized (object) {
            while (!isSucceed) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
