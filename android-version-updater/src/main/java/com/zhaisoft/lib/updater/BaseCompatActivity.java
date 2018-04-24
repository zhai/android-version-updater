package com.zhaisoft.lib.updater;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by zhai on 12/17/15.
 */

public class BaseCompatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String message){
        Toast.makeText(BaseCompatActivity.this, message,
                Toast.LENGTH_LONG).show();

    }

    public void showToast(@StringRes int  intString){
        Toast.makeText(BaseCompatActivity.this, getString(intString),
                Toast.LENGTH_LONG).show();

    }

}


