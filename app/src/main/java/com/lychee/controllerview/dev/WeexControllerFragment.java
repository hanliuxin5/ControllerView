package com.lychee.controllerview.dev;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.lychee.controllerview.R;
import com.lychee.controllerview.weex.AbstractWeexFragment;
import com.lychee.controllerview.weex.view.ControllerView;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by lychee on 17-4-5.
 */

public class WeexControllerFragment extends AbstractWeexFragment implements IWXRenderListener {

    @BindView(R.id.fl_controller)
    ControllerView flController;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.fl_weex)
    FrameLayout flWeex;
    Unbinder unbinder;

    private String sUri;
    private int width;
    private int height;

    private HashMap<String, String> params;
    private ProgressBar progressBar;

    public WeexControllerFragment() {

    }

    public static WeexControllerFragment newInstance(ControllerView cv) {
        Bundle bundle = new Bundle();
        bundle.putString("sUri", cv.getUrl());
        bundle.putInt("width", cv.getWidth());
        bundle.putInt("height", cv.getHeight());
        WeexControllerFragment controllerFragment = new WeexControllerFragment();
        controllerFragment.setArguments(bundle);
        return controllerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.sUri = bundle.getString("sUri");
            this.width = bundle.getInt("width");
            this.height = bundle.getInt("height");
            params = new HashMap<>();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weex, container, false);
        flController = (ControllerView) layout.findViewById(R.id.fl_controller);
        progressBar = (ProgressBar) layout.findViewById(R.id.progress);
        layout.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        setContainer(flController);
        flController.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        renderPageByURL(sUri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public String getsUri() {
        return sUri == null ? "" : sUri;
    }


    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        super.onViewCreated(instance, view);
        getContainer().setVisibility(View.INVISIBLE);

    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        super.onRenderSuccess(instance, width, height);
        getContainer().setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        super.onException(instance, errCode, msg);
        getContainer().setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


}
