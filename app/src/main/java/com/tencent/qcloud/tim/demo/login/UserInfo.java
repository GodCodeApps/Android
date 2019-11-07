package com.tencent.qcloud.tim.demo.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.qcloud.tim.demo.utils.Constants;

import java.util.Date;


public class UserInfo {

    private static UserInfo instance;
    private String account;
    private String password;
    private int room = 4321;
    private String replayUrl;
    public String userToken;
    public String userId;
    public String username;
    public String mobile;
    public String avatarUrl;
    public String signature;
    public Date birthday;
    public int sex;
    public String token;
    public String createTime;

    public UserInfo() {
    }

    public static UserInfo getInstance() {
        if (null == instance) {
            synchronized (UserInfo.class) {
                if (null == instance) {
                    instance = new UserInfo();
                }
            }
        }
        return instance;
    }

    public static void setInstance(UserInfo instance) {
        UserInfo.instance = instance;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getReplayUrl() {
        return replayUrl;
    }

    public void setReplayUrl(String replayUrl) {
        this.replayUrl = replayUrl;
    }

    public void writeToCache(Context context) {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.ACCOUNT, account);
        editor.putString(Constants.PWD, password);
        editor.putInt(Constants.ROOM, room);
        editor.commit();
    }

    public void clearCache(Context context) {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.clear();
        editor.commit();
    }

    public void getCache(Context context) {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        account = shareInfo.getString(Constants.ACCOUNT, null);
        password = shareInfo.getString(Constants.PWD, null);
        room = shareInfo.getInt(Constants.ROOM, 1234);
    }
}
