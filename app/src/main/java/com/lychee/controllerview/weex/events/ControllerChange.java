package com.lychee.controllerview.weex.events;


/**
 * Created by lychee on 17-4-6.
 */

public class ControllerChange {
    private String url;
    private String hostId;

    public ControllerChange(String url, String hostId) {
        this.url = url;
        this.hostId = hostId;
    }

    public String getUrl() {
        return url;
    }


    public String getHostId() {
        return hostId;
    }

}
