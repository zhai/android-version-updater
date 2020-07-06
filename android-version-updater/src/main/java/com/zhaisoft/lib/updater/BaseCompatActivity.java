package com.zhaisoft.lib.updater;

import android.os.Bundle;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zhai on 12/17/15.
 */

public class BaseCompatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String message) {
        Toast.makeText(BaseCompatActivity.this, message,
                Toast.LENGTH_LONG).show();

    }

    public void showToast(@StringRes int intString) {
        Toast.makeText(BaseCompatActivity.this, getString(intString),
                Toast.LENGTH_LONG).show();

    }

}


