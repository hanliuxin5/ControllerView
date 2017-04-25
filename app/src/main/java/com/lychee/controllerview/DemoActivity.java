package com.lychee.controllerview;

import com.lychee.controllerview.controllerView.ControllerViewActivity;

/**
 * Created by lychee on 17-4-25.
 */

public class DemoActivity extends ControllerViewActivity {
    @Override
    public void startLoad() {
        renderPageByURL("http://192.168.0.119:8080/dist/app.weex.js");
    }
}
