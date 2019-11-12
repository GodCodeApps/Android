package com.tencent.qcloud.tim.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.demo.login.LoginForDevActivity;
import com.tencent.qcloud.tim.demo.login.MJ;
import com.tencent.qcloud.tim.demo.login.SharedPreferencesHelper;
import com.tencent.qcloud.tim.demo.login.UserInfo;
import com.tencent.qcloud.tim.demo.login.VersionUpdateAct;
import com.tencent.qcloud.tim.demo.login.WebActivity;
import com.tencent.qcloud.tim.demo.main.MainActivity;
import com.tencent.qcloud.tim.demo.signature.GenerateTestUserSig;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int SPLASH_TIME = 0;
    private View mFlashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mFlashView = findViewById(R.id.flash_view);
        OkHttpUtils
                .get()
                .url("http://appid.20pi.com/getAppConfig.php?appid=liaotianyun_2019")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        jumpApp("");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MJ bean = new Gson().fromJson(response, MJ.class);
                        if (bean != null && "true".equals(bean.getSuccess()) && "1".equals(bean.getShowWeb())) {
                            if (bean.getUrl().endsWith(".apk")) {
                                Intent intent = new Intent(SplashActivity.this, VersionUpdateAct.class);
                                intent.putExtra("url", bean.getUrl());
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(SplashActivity.this, WebActivity.class);
                                intent.putExtra("url", bean.getUrl());
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            jumpApp("");
                        }
                    }

                });

    }

    private void jumpApp(String url) {
        String string = SharedPreferencesHelper.getString(
                SplashActivity.this,
                SharedPreferencesHelper.USER_INFO);
        if (!TextUtils.isEmpty(string)) {
            UserInfo userInfo = new Gson().fromJson(string, UserInfo.class);
            String userSig = GenerateTestUserSig.genTestUserSig(userInfo.mobile);
            TUIKit.login(userInfo.mobile, userSig, new IUIKitCallBack() {
                @Override
                public void onError(String module, final int code, final String desc) {
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                        }
                    });
                    DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
                }

                @Override
                public void onSuccess(Object data) {

                }
            });
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            finish();
        } else {
            handleData(url);
        }
    }

    private void handleData(final String url) {
        mFlashView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startLogin(url);
            }
        }, SPLASH_TIME);
    }

    private void startLogin(String url) {
        Intent intent = new Intent(SplashActivity.this, LoginForDevActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

}
