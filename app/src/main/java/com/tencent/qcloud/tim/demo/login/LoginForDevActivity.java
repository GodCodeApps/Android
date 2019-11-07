package com.tencent.qcloud.tim.demo.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.main.MainActivity;
import com.tencent.qcloud.tim.demo.signature.GenerateTestUserSig;
import com.tencent.qcloud.tim.demo.utils.Constants;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * <p>
 * Demo的登录Activity
 * 用户名可以是任意非空字符，但是前提需要按照下面文档修改代码里的 SDKAPPID 与 PRIVATEKEY
 * https://github.com/tencentyun/TIMSDK/tree/master/Android
 * <p>
 */

public class LoginForDevActivity extends Activity {

    private static final String TAG = LoginForDevActivity.class.getSimpleName();
    private static final int REQ_PERMISSION_CODE = 0x100;
    private Button mLoginView;
    private EditText mUserAccount;
    private EditText mUserPwd;
    private ZLoadingDialog dialog;

    //权限检查
    public static boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TUIKit.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                String[] permissionsArray = permissions.toArray(new String[1]);
                ActivityCompat.requestPermissions(activity,
                        permissionsArray,
                        REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_for_dev_layout);


        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }

        findViewById(R.id.tvRegist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForDevActivity.this, RegistActivity.class));
            }
        });
        mLoginView = findViewById(R.id.login_btn);
        // 用户名可以是任意非空字符，但是前提需要按照下面文档修改代码里的 SDKAPPID 与 PRIVATEKEY
        // https://github.com/tencentyun/TIMSDK/tree/master/Android
        mUserAccount = findViewById(R.id.login_user);
        mUserPwd = findViewById(R.id.login_pwd);
        findViewById(R.id.tvAgree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForDevActivity.this, AgreeAct.class));

            }
        });


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        checkPermission(this);
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mUserAccount.getText().toString();
                String pwd = mUserPwd.getText().toString();

                if (TextUtils.isEmpty(user) || user.length() < 11) {
                    Toast.makeText(LoginForDevActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginForDevActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.mobile = user;
                userInfo.setPassword(pwd);
                login(userInfo);

            }
        });
    }


    private void login(final UserInfo userInfo) {
        dialog = new ZLoadingDialog(this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setLoadingColor(getResources().getColor(R.color.colorAccent))//颜色
                .setHintText("登录中...")
                .show();
        OkHttpUtils
                .postString()
                .url(Api.BASE_API + Api.LOGIN)
                .content(new Gson().toJson(userInfo))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        Toast.makeText(LoginForDevActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        RespBase parseObject = new Gson().fromJson(response, RespBase.class);
                        if (parseObject.getCode() == 0) {
                            userInfo.token = parseObject.getToken();
                            userInfo.userId = parseObject.getUserId();
                            Log.e("onResponse", response);
                            // 获取userSig函数
                            String userSig = GenerateTestUserSig.genTestUserSig(mUserAccount.getText().toString());
                            TUIKit.login(mUserAccount.getText().toString(), userSig, new IUIKitCallBack() {
                                @Override
                                public void onError(String module, final int code, final String desc) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                                        }
                                    });
                                    DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
                                }

                                @Override
                                public void onSuccess(Object data) {
                                    SharedPreferencesHelper.setString(LoginForDevActivity.this, SharedPreferencesHelper.USER_INFO,
                                            new Gson().toJson(userInfo));
                                    SharedPreferences shareInfo = getSharedPreferences(Constants.USERINFO, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = shareInfo.edit();
                                    editor.putBoolean(Constants.AUTO_LOGIN, true);
                                    editor.commit();
                                    dialog.dismiss();
                                    Intent intent = new Intent(LoginForDevActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            dialog.dismiss();
                        }
                        ToastUtil.toastLongMessage(parseObject.getMsg() + "");
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    /**
     * 系统请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.toastLongMessage("未全部授权，部分功能可能无法使用！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
