package com.zhaisoft.lib.updater.example;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

import com.zhaisoft.lib.updater.AndroidUpdateSdk;
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
                AndroidUpdateSdk.getInstance().init(BlankActivity.this, true, "http://106.75.233.110/app/update.txt");
                LogUtil.e("hi");
            }
        }, 5000);


    }
}
