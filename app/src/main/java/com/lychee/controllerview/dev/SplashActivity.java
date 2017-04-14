package com.lychee.controllerview.dev;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lychee.controllerview.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button button2;


    private String sUrl = "http://192.168.0.110:8080/dist/controllerAct.js";
    private final String ACTION = "android.scheme.controller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.button, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent();
                Uri uri = Uri.parse(sUrl);
                intent.setData(uri);
                intent.setAction(ACTION);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent1 = new Intent();
                intent1.setClass(this, ControllerActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
