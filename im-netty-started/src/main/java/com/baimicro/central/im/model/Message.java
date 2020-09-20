package com.baimicro.central.im.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: hospital-cloud-platform
 * @description: 消息内容
 * @author: baiHoo.chen
 * @create: 2020-04-14
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Message implements Serializable {

    // 消息ID
    private String msgId;

    // 消息下标
    private Integer index;

    // 应用ID
    private String clientId;

    // 发送消息的用户id
    private String fromId;

    // 发送消息的用户名
    private String username;

    // 发送消息的用户头像
    private String avatar;

    // 接受消息的用户id
    private String toId;

    // 发送到那个应用ID
    private String toClientId;

    // 接受消息的用户ids
    private String[] toIds;

    // 接受消息的组ID
    private String groupId;

    // 接受消息的组Ids
    private String[] groupIds;

    // 消息类型：{0: 单聊，1: 群聊}
    private Integer chatType;

    // 消息内容
    private String msgContent;

    // 消息类型
    private String msgType;

    // 是否回撤
    private boolean isRetracement = false;

    // 服务端时间戳毫秒数
    private Long timestamp;

    // 消息的发送日期
    private String sendDate;

}
