package com.zhaisoft.lib.updater.imp;

import android.view.View;

import java.util.Calendar;

/**
 * @author zhai
 * @date 6/23/16
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    //最小的点击间隔

    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;


    public abstract void onNoDoubleClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}