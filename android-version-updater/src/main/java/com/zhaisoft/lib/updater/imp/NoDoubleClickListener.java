package com.zhaisoft.lib.updater.imp;

import android.view.View;

import java.util.Calendar;

/**
 * Created by zhai on 6/23/16. 防止一个按钮点击两次
 */
public abstract  class NoDoubleClickListener  implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 500; //500或者1000
    private long lastClickTime = 0;



    //抽象一个无连击事件方法，用于实现内容
    public abstract void onNoDoubleClick(View v);



    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);

        }
    }
}