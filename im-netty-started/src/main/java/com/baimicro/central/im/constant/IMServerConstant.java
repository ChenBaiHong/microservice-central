package com.baimicro.central.im.constant;

/**
 * @ClassName IMServerConstant
 * @Description TODO 常量配置
 * @Author baiHoo.chen
 * @Date 2019/7/1 16:54
 */
public interface IMServerConstant {

    /**
     * 协议名字(可以随便取，主要用于开发人员辨识)
     */
    String protocolName = "x-ws-protocol";

    String version = "1.0.0";

    /**
     * 字符集
     */
    String charset = "utf-8";

    String REDIS_CLIENT_ID = "im:clientId:";
    String REDIS_USER_CHANNEL = "im:userChannel:";

    String REDIS_WEBSOCKET_COLONY = "im:websocket:colony";
    String REDIS_WEBSOCKET_COLONY_NODENAME = "im:websocket:colony:nodename:";


    String RESTFUL_SEND_MESSAGE_PATH = "/im/message/sendMessage";


}
