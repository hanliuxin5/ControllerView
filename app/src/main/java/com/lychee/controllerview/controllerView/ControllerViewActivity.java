package com.lychee.controllerview.controllerView;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lychee.controllerview.R;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

/**
 * Created by lychee on 17-4-25.
 */

public abstract class ControllerViewActivity extends AppCompatActivity implements IWXRenderListener {

    private static final String TAG = "ControllerViewActivity";

    @BindView(R.id.fl_main)
    FrameLayout flMain;

    private ViewGroup mContainer;
    private boolean isRenderSuccess = false;
    protected WXSDKInstance mInstance;

    protected WXAnalyzerDelegate mWxAnalyzerDelegate;

    private ArrayList<ControllerViewFragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //取消系统自动对fragment的保存,因为fragment会恢复创建
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);

        createWeexInstance();
        mInstance.onActivityCreate();
        mWxAnalyzerDelegate = new WXAnalyzerDelegate(this);
        mWxAnalyzerDelegate.onCreate();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.activity_controller_weex);
        ButterKnife.bind(this);
        setContainer(flMain);
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        } else {
            mFragments.clear();
        }
        EventBus.getDefault().register(this);
        startLoad();
    }

    abstract public void startLoad();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onControllerChanged(ControllerChange cc) {
        if (isRenderSuccess && cc.getHostId().equals(mInstance.getInstanceId())) {
            loadControllerView(cc.getUrl(), cc.getTag());
        }
    }


    private synchronized void loadControllerView(String uri, String tag) {
        //父视图渲染完成，渲染子视图
        ControllerView vg = (ControllerView) getContainer().findViewWithTag(tag);
        if (vg != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < mFragments.size(); i++) {
                ControllerViewFragment fragment = mFragments.get(i);
                if (fragment.getTag().startsWith(tag + ":") && !fragment.getsUri().equals(uri)) {
                    fragmentTransaction
                            .hide(fragment);
                } else if (fragment.getTag().startsWith(tag + ":") && fragment.getsUri().equals(uri)) {
                    fragmentTransaction
                            .show(fragment);
                }
            }
            fragmentTransaction.commit();

            if (!searchFragment(uri, tag) || mFragments.size() == 0) {
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                ControllerViewFragment fragment = ControllerViewFragment.newInstance(vg);
                fragmentTransaction2
                        .add(R.id.fl_main, fragment, tag + ":" + vg.getUrl())
                        .commit();
                mFragments.add(fragment);
            }
        }
    }

    private boolean searchFragment(String url, String tag) {
        ControllerViewFragment controllerFragment;
        for (int i = 0; i < mFragments.size(); i++) {
            controllerFragment = mFragments.get(i);
            if (controllerFragment.getTag().startsWith(tag + ":") && controllerFragment.getsUri().equals(url)) {
                return true;
            }
        }
        return false;
    }

    public final void setContainer(ViewGroup container) {
        mContainer = container;
    }

    public final ViewGroup getContainer() {
        return mContainer;
    }

    public void destroyWeexInstance() {
        if (mInstance != null) {
            mInstance.registerRenderListener(null);
            mInstance.destroy();
            mInstance = null;
        }
    }

    public void createWeexInstance() {
        destroyWeexInstance();

        //没太明白这里是干啥的,所以我就注释了...
//        Rect outRect = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        mInstance = new WXSDKInstance(this);
        mInstance.registerRenderListener(this);
    }

    public void renderPage(String template, String source) {
        renderPage(template, source, null);
    }

    public void renderPage(String template, String source, String jsonInitData) {
        AssertUtil.throwIfNull(mContainer, new RuntimeException("Can't render page, container is null"));
        Map<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, source);
        mInstance.setTrackComponent(true);
        mInstance.render(
                getPageName(),
                template,
                options,
                jsonInitData,
                WXRenderStrategy.APPEND_ASYNC);
    }

    public void renderPageByURL(String url) {
        renderPageByURL(url, null);
    }

    public void renderPageByURL(String url, String jsonInitData) {
        AssertUtil.throwIfNull(mContainer, new RuntimeException("Can't render page, container is null"));
        Map<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, url);
        mInstance.setTrackComponent(true);
        mInstance.renderByUrl(
                getPageName(),
                url,
                options,
                jsonInitData,
                WXRenderStrategy.APPEND_ASYNC);
    }

    public String getPageName() {
        return TAG;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mInstance != null) {
            mInstance.onActivityStart();
        }
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInstance != null) {
            mInstance.onActivityResume();
        }
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mInstance != null) {
            mInstance.onActivityPause();
        }
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mInstance != null) {
            mInstance.onActivityStop();
        }
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mInstance != null) {
            mInstance.onActivityDestroy();
        }
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onDestroy();
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance wxsdkInstance, View view) {
        View wrappedView = null;
        if (mWxAnalyzerDelegate != null) {
            wrappedView = mWxAnalyzerDelegate.onWeexViewCreated(wxsdkInstance, view);
        }
        if (wrappedView != null) {
            view = wrappedView;
        }
        if (mContainer != null) {
            mContainer.removeAllViews();
            mContainer.addView(view);
        }
    }


    @Override
    public void onRefreshSuccess(WXSDKInstance wxsdkInstance, int i, int i1) {

    }

    @Override
    @CallSuper
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onWeexRenderSuccess(instance);
        }
        isRenderSuccess = true;
        ControllerView vg;
        String tag;
        ViewGroup container = getContainer();
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                tag = ControllerView.TAG;
                vg = (ControllerView) container.findViewWithTag(tag);
            } else {
                tag = ControllerView.TAG + i;
                vg = (ControllerView) container.findViewWithTag(tag);
            }
            if (vg != null) {
                final ControllerView finalVg = vg;
                final String finalTag = tag;
                Completable
                        .timer(50, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                EventBus.getDefault().post(new ControllerChange(finalVg.getUrl()
                                        , mInstance.getInstanceId(), finalTag));

                            }
                        });
            }

        }
    }

    @Override
    @CallSuper
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onException(instance, errCode, msg);
        }
        isRenderSuccess = false;
    }

    @Override
    @CallSuper
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return (mWxAnalyzerDelegate != null && mWxAnalyzerDelegate.onKeyUp(keyCode, event)) || super.onKeyUp(keyCode, event);
    }
}
