package com.zhaisoft.lib.updater.example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.zhaisoft.lib.updater.AndroidUpdateSDK;

public class BlankActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        setContentView(R.layout.activity_blank);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);

        //setTitle("Android Version Updater");

        //requestRightAndDo();
        //AndroidUpdateSDK.getInstance().init(BlankActivity.this, true, "http://app.zhaisoft.com/app_update/wechat-tool/update.txt");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUpdateSDK.getInstance().init(BlankActivity.this, true, "http://app.zhaisoft.com/app_update/wechat-tool/update.txt");

            }
        });
    }

    private void requestRightAndDo() {

//        AndPermission.with(this)
//                .permission(Permission.WRITE_EXTERNAL_STORAGE)
//
//                // 准备方法，和 okhttp 的拦截器一样，在请求权限之前先运行改方法，已经拥有权限不会触发该方法
//                .rationale((context, permissions, executor) -> {
//                    // 此处可以选择显示提示弹窗
//                    executor.execute();
//                })
//                // 用户给权限了
//                .onGranted(permissions -> {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //ToastUtils.show(WelcomeActivity.this, ("用户给权限啦"));
//                                }
//                            });
//                            //ToastUtils.show(this, ("用户给权限啦"));
//                            checlUpdate();
//                        }
//                )
//                // 用户拒绝权限，包括不再显示权限弹窗也在此列
//                .onDenied(permissions -> {
//                    // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
//                    if (AndPermission.hasAlwaysDeniedPermission(BlankActivity.this, permissions)) {
//                        // 打开权限设置页
//                        //ToastUtils.show(this, "需要权限，不然程序无法运行");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(BlankActivity.this, ("需要权限，不然程序无法运行"), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        AndPermission.permissionSetting(BlankActivity.this).execute();
//                        return;
//                    }
//                    //ToastUtils.show(this, "需要权限，不然程序无法运行");
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(BlankActivity.this, ("需要权限，不然程序无法运行"), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                })
//                .start();
    }


}
