package com.baimicro.central.common.lock;

import java.util.concurrent.TimeUnit;

/**
 *
 * 分布式锁顶级接口
 * 例如：
 * RETRY_TIMES=100，SLEEP_MILLIS=100
 * RETRY_TIMES * SLEEP_MILLIS = 10000 意味着如果一直获取不了锁，最长会等待10秒后抛超时异常
 *
 *
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO
 * version 0.1
 */
public interface DistributedLock {
    /**
     * 获取锁，如果获取不成功则一直等待直到lock被获取
     * @param key 锁的key
     * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit {@code leaseTime} 参数的时间单位
     * @param isFair 是否公平锁
     * @return 锁对象
     */
    Object lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;
    Object lock(String key, long leaseTime, TimeUnit unit) throws Exception;
    Object lock(String key, boolean isFair) throws Exception;
    Object lock(String key) throws Exception;

    /**
     * 尝试获取锁，如果锁不可用则等待最多waitTime时间后放弃
     * @param key 锁的key
     * @param waitTime 获取锁的最大尝试时间(单位 {@code unit})
     * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit {@code waitTime} 和 {@code leaseTime} 参数的时间单位
     * @return 锁对象，如果获取锁失败则为null
     */
    Object tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;
    Object tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws Exception;
    Object tryLock(String key, long waitTime, TimeUnit unit, boolean isFair) throws Exception;
    Object tryLock(String key, long waitTime, TimeUnit unit) throws Exception;

    /**
     * 释放锁
     * @param lock 锁对象
     */
    void unlock(Object lock) throws Exception;
}

