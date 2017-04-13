package com.lychee.controllerview.weex.a_javabean;

/**
 * Created by lychee on 17-4-11.
 */

public class WeexController {
    private String uri;
    private String groupId;

    public WeexController(String uri, String groupId) {
        this.uri = uri;
        this.groupId = groupId;
    }

    public String getUri() {
        return uri;
    }


    public String getGroupId() {
        return groupId;
    }

}
