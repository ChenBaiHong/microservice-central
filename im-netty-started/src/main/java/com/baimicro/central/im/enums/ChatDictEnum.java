package com.baimicro.central.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ChatDictEnum {


    CONTENT_TYPE("CONTENT_TYPE", "内容类型: {1: 文字内容, 2: 语音内容, 3: 文件内容, 4: 视频内容, 5: 图片，6: 预约推送 }"), CHAT_IDENT("CHAT_IDENT", "聊天身份: {0:患者，1:医生}"), READ_STATUS("READ_STATUS", "消息读状态: {0: 已读 , 1: 未读}"), CHAT_TYPE("CHAT_TYPE", "消息类型:{1: 患者对医生发消息 , 2:医生对患者发消息 , 3:患者结束发消息 ， 4:医生结束发消息}"), GENDER("GENDER", "性别, {0:男，1:女，2:保密}"), WHO_END("WHO_END", "谁主动结束了咨询，{1:医生，2:微信用户}"), IDENT_TYPE("IDENT_TYPE", "消息身份类型:{1: 患者 , 2:医生}");


    private String dictTypeCode;
    private String dictText;
}
