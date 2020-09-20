package com.baimicro.central.im.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * @program: hospital-cloud-platform
 * @description:
 * @author: baiHoo.chen
 * @create: 2020-04-14
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionGroup implements Serializable {

    private String imUserId;

    private String clientId;

    private String channelId;

    private String host;

    private String port;

    public SessionGroup() {
    }

    /**
     * @param imUserId
     * @param clientId
     * @param channelId
     */
    public SessionGroup(String imUserId, String clientId, String channelId) {
        this.imUserId = imUserId;
        this.clientId = clientId;
        this.channelId = channelId;
    }

    public SessionGroup(String imUserId, String clientId, String channelId, String host, String port) {
        this.imUserId = imUserId;
        this.clientId = clientId;
        this.channelId = channelId;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionGroup that = (SessionGroup) o;
        return Objects.equals(imUserId, that.imUserId) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imUserId, clientId, channelId, host, port);
    }
}
