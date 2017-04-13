package com.lychee.controllerview.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lychee.controllerview.weex.events.ControllerChange;
import com.lychee.controllerview.weex.view.ControllerView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lychee on 17-4-5.
 */

public class Controller extends WXComponent {
    private String instanceId;

    public Controller(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
        this.instanceId = instance.getInstanceId();
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        ControllerView view = new ControllerView(context);
        view.setHostId(instanceId);
        return view;
    }

    @WXComponentProp(name = "src")
    public void setSrc(String src) {
        ControllerView cv = (ControllerView) getRealView();
        cv.setUrl(src);
        EventBus.getDefault().post(new ControllerChange(src, cv.getHostId()));
    }


    //此tag必须为 "lychee"
    @WXComponentProp(name = "tag")
    public void setTag(String tag) {
        ControllerView cv = (ControllerView) getRealView();
        cv.setTag(tag);
    }


}
