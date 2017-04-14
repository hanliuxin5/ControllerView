package com.lychee.controllerview.dev;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.lychee.controllerview.R;
import com.lychee.controllerview.weex.AbstractWeexActivity;
import com.lychee.controllerview.weex.events.ControllerChange;
import com.lychee.controllerview.weex.view.ControllerView;
import com.taobao.weex.WXSDKInstance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lychee on 17-4-5.
 */

public class WeexControllerActivity extends AbstractWeexActivity {

    @BindView(R.id.fl_controller)
    FrameLayout flController;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private boolean isRenderSuccess = false;
    private ArrayList<WeexControllerFragment> mFragments;

    private HashMap<String, String> params;
    private Uri uri;

    public final static String URL = "url";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL, params.get(URL));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //取消系统自动对fragment的保存,因为fragment会恢复创建
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //从后台恢复时初始化数据(保险起见)
            if (savedInstanceState.containsKey(URL)) {
                uri = Uri.parse(savedInstanceState.getString(URL));
                params = initUri(uri);
            }
        }
        setContentView(R.layout.activity_controller_weex);
        ButterKnife.bind(this);

        setContainer(flController);
        mFragments = new ArrayList<>();
        EventBus.getDefault().register(this);
        srl.setEnabled(false);

        //正常打开时初始化数据
        if (getIntent() != null) {
            uri = getIntent().getData();
            if (uri == null) {
                return;
            }
            params = initUri(uri);
        }
        //初始化界面
        initUI();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private HashMap<String, String> initUri(Uri uri) {
        HashMap<String, String> params = new HashMap<>();
        params.put(URL, uri.toString());
        return params;
    }

    public void initUI() {
        getContainer().setVisibility(View.INVISIBLE);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
            }
        });
        renderPageByURL(params.get(URL));
//        renderPage(WXFileUtils.loadAsset(params.get(URL), this), "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onControllerChanged(ControllerChange cc) {
        if (isRenderSuccess && cc.getHostId().equals(mInstance.getInstanceId())) {
            loadControllerView(cc.getUrl());
        }
    }


    private void loadControllerView(String uri) {
        //父视图渲染完成，渲染子视图
        ControllerView vg = (ControllerView) getContainer().findViewWithTag(ControllerView.TAG);
        if (vg != null) {
            for (int i = 0; i < mFragments.size(); i++) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (mFragments.get(i).getsUri().equals(uri)) {
                    fragmentTransaction
                            .show(mFragments.get(i));
                } else {
                    fragmentTransaction
                            .hide(mFragments.get(i));
                }
                fragmentTransaction.commit();
            }
            if (!searchFragment(uri) || mFragments.size() == 0) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                WeexControllerFragment fragment = WeexControllerFragment.newInstance(vg);
                fragmentTransaction
                        .add(R.id.fl_controller, fragment, vg.getUrl())
                        .commit();
                mFragments.add(fragment);
            }
        }
    }

    private boolean searchFragment(String url) {
        WeexControllerFragment controllerFragment;
        for (int i = 0; i < mFragments.size(); i++) {
            controllerFragment = mFragments.get(i);
            if (controllerFragment.getsUri().equals(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onViewCreated(WXSDKInstance wxsdkInstance, View view) {
        super.onViewCreated(wxsdkInstance, view);
        getContainer().setVisibility(View.INVISIBLE);

    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        super.onRenderSuccess(instance, width, height);
        isRenderSuccess = true;
        final ControllerView vg = (ControllerView) getContainer().findViewWithTag(ControllerView.TAG);
        if (vg != null) {
            //为了解决一个暂时无解的bug，如果如果直接调用loadControllerView
            //第一个fragment的视图经常性的不能加载出来
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(new ControllerChange(vg.getUrl()
                            , mInstance.getInstanceId()));
                }
            }).start();
        }
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(false);
                getContainer().setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        super.onException(instance, errCode, msg);
        isRenderSuccess = false;
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(false);
            }
        });
    }

}
