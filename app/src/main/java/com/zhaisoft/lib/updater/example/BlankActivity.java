package com.zhaisoft.lib.updater.example;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.zhaisoft.lib.updater.AndroidUpdateSDK;
import com.zhaisoft.lib.updater.BaseCompatActivity;
import com.zhaisoft.lib.updater.log.LogUtil;

public class BlankActivity extends BaseCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUpdateSDK.getInstance().init(BlankActivity.this, true, "http://106.75.233.110/app/update.txt");
                LogUtil.e("hi");
            }
        }, 5000);


    }
}
