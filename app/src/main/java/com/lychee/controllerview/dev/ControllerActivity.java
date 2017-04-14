package com.lychee.controllerview.dev;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lychee.controllerview.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lychee on 17-4-14.
 */

public class ControllerActivity extends AppCompatActivity {
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;

    private String url = "";
    private ControllerFragment fragment1;
    private WeexControllerFragment2 fragment2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        ButterKnife.bind(this);


        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "请替换url为你自己的线上 *.js 路径", Toast.LENGTH_SHORT).show();
        }
        if (savedInstanceState != null) {
            fragment1 = (ControllerFragment) getSupportFragmentManager().findFragmentByTag("0");
            fragment2 = (WeexControllerFragment2) getSupportFragmentManager().findFragmentByTag("1");
            getSupportFragmentManager().beginTransaction()
                    .show(fragment1)
                    .hide(fragment2)
                    .commit();
        } else {
            fragment1 = new ControllerFragment();
            fragment2 = WeexControllerFragment2.newInstance(url);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl, fragment1, "0")
                    .add(R.id.fl, fragment2, "1")
                    .hide(fragment2)
                    .commit();
        }
    }

    @OnClick({R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                getSupportFragmentManager().beginTransaction()
                        .show(fragment1)
                        .hide(fragment2)
                        .commit();
                break;
            case R.id.bt2:
                getSupportFragmentManager().beginTransaction()
                        .show(fragment2)
                        .hide(fragment1)
                        .commit();
                break;
        }
    }
}
