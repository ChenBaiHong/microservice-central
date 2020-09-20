package com.baimicro.central.im.enums;


import java.util.HashMap;


public class ChatDictUtil {
    // 数据字典
    private static final HashMap<String, HashMap<Integer, String>> dictMap = new HashMap<>();
    // 内容类型
    private static final HashMap<Integer, String> CONTENT_TYPE = new HashMap<>();
    // 诊聊身份
    private static final HashMap<Integer, String> CHAT_IDENT = new HashMap<>();
    // 是否已读
    private static final HashMap<Integer, String> READ_STATUS = new HashMap<>();

    private static final HashMap<Integer, String> CHAT_TYPE = new HashMap<>();

    private static final HashMap<Integer, String> GENDER = new HashMap<>();

    private static final HashMap<Integer, String> WHO_END = new HashMap<>();

    private static final HashMap<Integer, String> IDENT_TYPE = new HashMap<>();

    static {
        //
        CONTENT_TYPE.put(1, "文字内容");
        CONTENT_TYPE.put(2, "语音内容");
        CONTENT_TYPE.put(3, "文件内容");
        CONTENT_TYPE.put(4, "视频内容");
        CONTENT_TYPE.put(5, "图片");
        CONTENT_TYPE.put(6, "预约推送");

        //
        CHAT_IDENT.put(0, "患者");
        CHAT_IDENT.put(1, "医生");
        //
        READ_STATUS.put(0, "已读");
        READ_STATUS.put(1, "未读");

        CHAT_TYPE.put(1, "患者对医生发消息");
        CHAT_TYPE.put(2, "医生对患者发消息");
        CHAT_TYPE.put(3, "患者结束发消息");
        CHAT_TYPE.put(4, "医生结束发消息");

        GENDER.put(0, "男");
        GENDER.put(1, "女");
        GENDER.put(2, "保密");

        WHO_END.put(1, "医生");
        WHO_END.put(2, "微信用户");


        IDENT_TYPE.put(1, "患者");
        IDENT_TYPE.put(2, "医生");

        // 数据字典 map 添加
        dictMap.put("CONTENT_TYPE", CONTENT_TYPE);
        dictMap.put("CHAT_IDENT", CHAT_IDENT);
        dictMap.put("READ_STATUS", READ_STATUS);
        dictMap.put("CHAT_TYPE", CHAT_TYPE);
        dictMap.put("GENDER", GENDER);
        dictMap.put("WHO_END", WHO_END);
        dictMap.put("IDENT_TYPE", IDENT_TYPE);
    }

    /**
     * 获取 数据字典名称 通过数据字典ID
     *
     * @param dictEnum
     * @param DictId
     * @return
     */
    public static String getDictNameByDictId(ChatDictEnum dictEnum, Integer DictId) {

        return dictMap.get(dictEnum.getDictTypeCode()).get(DictId);
    }
}
