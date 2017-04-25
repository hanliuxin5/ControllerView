package com.lychee.controllerview.controllerView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lychee.controllerview.R;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by lychee on 17-4-25.
 */

public class ControllerViewFragment extends Fragment implements IWXRenderListener {
    private static final String TAG = "ControllerViewFragment";

    @BindView(R.id.fragment_fl)
    FrameLayout fragmentFl;
    Unbinder unbinder;

    protected WXAnalyzerDelegate mWxAnalyzerDelegate;
    private ViewGroup mContainer;
    protected WXSDKInstance mInstance;
    private Context _mActivity;

    private String sUri;
    private int width;
    private int height;
    private int paddingTop;
    private int paddingBottom;
    private int paddingRight;
    private int paddingLeft;
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;

    public ControllerViewFragment() {

    }

    public static ControllerViewFragment newInstance(ControllerView cv) {
        Bundle bundle = new Bundle();
        initLayoutParams(bundle, cv);
        ControllerViewFragment controllerFragment = new ControllerViewFragment();
        controllerFragment.setArguments(bundle);
        return controllerFragment;
    }

    private static void initLayoutParams(Bundle bundle, ControllerView cv) {

        bundle.putString("sUri", cv.getUrl());
        bundle.putInt("width", cv.getWidth());
        bundle.putInt("height", cv.getHeight());

        bundle.putInt("padding-top", cv.getPaddingTop());
        bundle.putInt("padding-left", cv.getPaddingLeft());
        bundle.putInt("padding-right", cv.getPaddingRight());
        bundle.putInt("padding-bottom", cv.getPaddingBottom());

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cv.getLayoutParams();
        bundle.putInt("leftMargin", layoutParams.leftMargin);
        bundle.putInt("rightMargin", layoutParams.rightMargin);
        bundle.putInt("topMargin", layoutParams.topMargin);
        bundle.putInt("bottomMargin", layoutParams.bottomMargin);
    }

    private void loadViewParams(View layout) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = bottomMargin;
        layout.setLayoutParams(layoutParams);
        layout.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _mActivity = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createWeexInstance();
        mInstance.onActivityCreate();
        mWxAnalyzerDelegate = new WXAnalyzerDelegate(_mActivity);
        mWxAnalyzerDelegate.onCreate();

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.sUri = bundle.getString("sUri");
            this.width = bundle.getInt("width");
            this.height = bundle.getInt("height");
            this.paddingTop = bundle.getInt("padding-top");
            this.paddingLeft = bundle.getInt("padding-left");
            this.paddingRight = bundle.getInt("padding-right");
            this.paddingBottom = bundle.getInt("padding-bottom");

            this.leftMargin = bundle.getInt("leftMargin");
            this.rightMargin = bundle.getInt("rightMargin");
            this.topMargin = bundle.getInt("topMargin");
            this.bottomMargin = bundle.getInt("bottomMargin");

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_controller_weex, container, false);
        unbinder = ButterKnife.bind(this, layout);
        loadViewParams(layout);
        setContainer(fragmentFl);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        renderPageByURL(sUri);
    }

    public void createWeexInstance() {
        destroyWeexInstance();
        mInstance = new WXSDKInstance(_mActivity);
        mInstance.registerRenderListener(this);
    }

    public void destroyWeexInstance() {
        if (mInstance != null) {
            mInstance.registerRenderListener(null);
            mInstance.destroy();
            mInstance = null;
        }
    }

    public String getPageName() {
        return TAG;
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


    public final void setContainer(ViewGroup container) {
        mContainer = container;
    }

    public final ViewGroup getContainer() {
        return mContainer;
    }

    public String getsUri() {
        return sUri == null ? "" : sUri;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mInstance != null) {
            mInstance.onActivityDestroy();
        }
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onDestroy();
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {

        View wrappedView = null;
        if (mWxAnalyzerDelegate != null) {
            wrappedView = mWxAnalyzerDelegate.onWeexViewCreated(instance, view);
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
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onWeexRenderSuccess(instance);
        }
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        if (mWxAnalyzerDelegate != null) {
            mWxAnalyzerDelegate.onException(instance, errCode, msg);
        }
    }


}
