package com.lychee.controllerview.dev;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    private String sUrl = "http://192.168.0.110:8080/dist/controllerAct.js";
    private final String ACTION = "android.scheme.controller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        Uri uri = Uri.parse(sUrl);
        intent.setData(uri);
        intent.setAction(ACTION);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }

}
