package com.baimicro.central.im.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UsedInfo
 * @Description TODO 通讯的json 封装
 * @Author baiHoo.chen
 * @Date 2019/7/1 17:40
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedInfo implements Serializable {

    // 处理消息 {0: 在线消息处理，1：离线消息处理}
    private Integer code = 0;

    /**
     * 信息
     */
    private Message message;

    public UsedInfo() {
    }

    public UsedInfo(Integer code) {
        this.code = code;
    }
}
