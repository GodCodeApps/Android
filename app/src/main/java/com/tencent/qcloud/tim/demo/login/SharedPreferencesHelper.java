package com.tencent.qcloud.tim.demo.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
   public static String USER_DATA = "user_data";
    public  static String USER_INFO = "user_info";

    public static void setString(Activity context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                USER_DATA,
                Context.MODE_PRIVATE
        ).edit();
        editor.putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences editor = context.getSharedPreferences(
                USER_DATA,
                Context.MODE_PRIVATE
        );
        return editor.getString(key, "");
    }

    public static void clearAll(Context context) {
        SharedPreferences editor = context.getSharedPreferences(
                USER_DATA,
                Context.MODE_PRIVATE
        );
        editor.edit().clear().commit();
    }
}
