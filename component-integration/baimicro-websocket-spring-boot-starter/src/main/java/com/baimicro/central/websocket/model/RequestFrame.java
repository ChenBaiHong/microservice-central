package com.baimicro.central.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @project: hualida-scada
 * @author: chen.baihoo
 * @date: 2020/8/31
 * @Description: TODO 请求帧
 * version 0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFrame implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 定义报文帧
     */
//    private PacketFrame packetFrame;

    /**
     * 解析的内容
     */
//    private String analysisContent;
}
