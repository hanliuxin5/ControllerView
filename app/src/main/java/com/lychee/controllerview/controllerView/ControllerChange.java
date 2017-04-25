package com.lychee.controllerview.controllerView;


/**
 * Created by lychee on 17-4-6.
 */

public class ControllerChange {
    private String url;
    private String hostId;
    private String tag;

    public ControllerChange(String url, String hostId, String tag) {
        this.url = url;
        this.hostId = hostId;
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }


    public String getHostId() {
        return hostId;
    }

    public String getTag() {
        return tag;
    }

}
