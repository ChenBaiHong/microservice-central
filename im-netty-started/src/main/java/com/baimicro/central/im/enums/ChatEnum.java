package com.baimicro.central.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author baiHoo
 * @Description //TODO 封装的信息类型 UTILS
 * @Date 20:34 2019/6/19
 **/
@Getter
@AllArgsConstructor
public enum ChatEnum {

    PATIENT(1, "患者咨询"),
    DOCTOR(2, "医生回复"),
    READED(0, "已读"),
    UNREAD(1, "未读"),

    MSG_SINGLE(0, "单聊"),
    MSG_GROUP(1, "群聊"),

    MSG_ONLINE(0, "在线消息"),
    MSG_OFFLINE(1, "离线消息"),

    CONTENT_TYPE_TEXT(1, "内容文本类型"),
    CONTENT_TYPE_VOICE(2, "内容语音类型");


    private Integer dictCode;
    private String dictText;
}
