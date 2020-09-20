package com.baimicro.central.websocket.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @project: hualida-scada
 * @author: chen.baihoo
 * @date: 2020/8/25
 * @Description: TODO 十进制转换其他进制工具
 * version 0.1
 */
public class DecimalTools {

    /**
     * 十六进制字符转十进制
     *
     * @param content
     * @return
     */
    public int hex2Dec(String content) {
        int number = 0;
        String[] HighLetter = {"A", "B", "C", "D", "E", "F"};
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i <= 9; i++) {
            map.put(i + "", i);
        }
        for (int j = 10; j < HighLetter.length + 10; j++) {
            map.put(HighLetter[j - 10], j);
        }
        String[] str = new String[content.length()];
        for (int i = 0; i < str.length; i++) {
            str[i] = content.substring(i, i + 1);
        }
        for (int i = 0; i < str.length; i++) {
            number += map.get(str[i]) * Math.pow(16, str.length - 1 - i);
        }
        return number;
    }

    /**
     * 十六进制转化成 字节
     *
     * @param src
     * @return
     */
    public byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * 给客户端写数据
     *
     * @param receiveStr
     * @param channel
     * @param mark
     */
    public void writeToClient(final String receiveStr, ChannelHandlerContext channel, final String mark) {
        try {
            ByteBuf bufff = Unpooled.buffer();//netty需要用ByteBuf传输
            bufff.writeBytes(hexString2Bytes(receiveStr));//对接需要16进制
            channel.writeAndFlush(bufff).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    StringBuilder sb = new StringBuilder();
                    if (!StringUtils.isEmpty(mark)) {
                        sb.append("【").append(mark).append("】");
                    }
                    if (future.isSuccess()) {
                        System.out.println(sb.toString() + "回写成功" + receiveStr);

                    } else {
                        System.out.println(sb.toString() + "回写失败" + receiveStr);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用通用writeToClient()异常" + e.getMessage());

        }
    }
}
