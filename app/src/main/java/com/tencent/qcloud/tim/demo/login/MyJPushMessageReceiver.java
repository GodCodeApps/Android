package com.tencent.qcloud.tim.demo.login;

import android.content.Context;
import android.content.Intent;

import com.tencent.qcloud.tim.demo.SplashActivity;

import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJPushMessageReceiver extends JPushMessageReceiver {
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        context.startActivity(new Intent(context, SplashActivity.class));
    }
}
