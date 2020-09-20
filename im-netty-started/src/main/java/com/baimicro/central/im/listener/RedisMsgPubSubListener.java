package com.baimicro.central.im.listener;

import io.lettuce.core.pubsub.RedisPubSubListener;
import io.netty.channel.Channel;


/**
 * @program: hospital-cloud-platform
 * @description: Redis 发布订阅监听
 * @author: baiHoo.chen
 * @create: 2020-04-16
 **/
public class RedisMsgPubSubListener implements RedisPubSubListener<String, Channel> {

    @Override
    public void message(String channel, Channel message) {

    }

    @Override
    public void message(String pattern, String channel, Channel message) {

    }

    @Override
    public void subscribed(String channel, long count) {

    }

    @Override
    public void psubscribed(String pattern, long count) {

    }

    @Override
    public void unsubscribed(String channel, long count) {

    }

    @Override
    public void punsubscribed(String pattern, long count) {

    }
}
