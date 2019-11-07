package com.tencent.qcloud.tim.demo.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.tencent.qcloud.tim.uikit.base.ITitleBarLayout.POSITION.MIDDLE;

public class RegistActivity extends Activity {

    private EditText inputName;
    private EditText inputTel;
    private EditText inputPwd;
    private EditText inpuedPwdAgain;
    private ZLoadingDialog dialog;
    private ImageView ivAvatar;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activity);
        TitleBarLayout titleBarLayout = findViewById(R.id.title);
        inputName = findViewById(R.id.inputName);
        inputTel = findViewById(R.id.inputTel);
        inputPwd = findViewById(R.id.inputPwd);
        inpuedPwdAgain = findViewById(R.id.edPwdAgain);
        ivAvatar = findViewById(R.id.ivAvatar);

        Button btnComple = findViewById(R.id.btnComple);
        userInfo = new UserInfo();

        titleBarLayout.setTitle("注册", MIDDLE);
        titleBarLayout.setRightIcon(0);
        titleBarLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
        btnComple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edName = inputName.getText().toString().trim();
                String edTel = inputTel.getText().toString().trim();
                String edPwd = inputPwd.getText().toString().trim();
                String edPwdAgain = inpuedPwdAgain.getText().toString().trim();
                if (TextUtils.isEmpty(userInfo.avatarUrl)) {
                    Toast.makeText(RegistActivity.this, "请上传头像", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edName)) {
                    Toast.makeText(RegistActivity.this, "请输入昵称", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edTel) || edTel.length() < 11) {
                    Toast.makeText(RegistActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edPwd) || edPwd.length() < 6 || edPwd.length() > 8 || TextUtils.isEmpty(
                        edPwdAgain
                ) || edPwdAgain.length() < 6 || edPwdAgain.length() > 8
                ) {
                    Toast.makeText(RegistActivity.this, "请输入正确密码", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!edPwd.equals(edPwdAgain)) {
                    Toast.makeText(RegistActivity.this, "两次密码不一样", Toast.LENGTH_LONG).show();
                    return;
                }
                userInfo.username = edName;
                userInfo.mobile = edTel;
                userInfo.setPassword(edPwd);
                register(userInfo);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            String path = PathUtils.getPath(this, data.getData());
            dialog = new ZLoadingDialog(RegistActivity.this);
            dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setLoadingColor(getResources().getColor(R.color.colorAccent))//颜色
                    .setHintText("头像上传中...")
                    .show();
//            val file = File("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1564069795027.jpg")
            File file = new File(path);
            GlideEngine.loadImage(ivAvatar, Uri.parse(path));
            upload(file);
        } else if (requestCode == 205 && resultCode == RESULT_OK) {
            finish();
        }
    }

    void upload(File file) {

        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), filebody)
                .build();
        Request request = new Request.Builder()
                .url(Api.FILE_UPLOAD)
                .post(body)
                .build();
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onResponseonFailure", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse", string);
                ImageUpload upload = new Gson().fromJson(string, ImageUpload.class);
                userInfo.avatarUrl = upload.url;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        GlideEngine.loadImage(ivAvatar, Uri.parse(userInfo.avatarUrl));
//                        Glide.with(_mActivity).load(upload.url).into(ivImageHead)
                    }
                });

            }
        });
    }

    private void register(UserInfo userInfo) {
        dialog = new ZLoadingDialog(this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setLoadingColor(getResources().getColor(R.color.colorAccent))//颜色
                .setHintText("注册中...")
                .show();
        OkHttpUtils
                .postString()
                .url(Api.BASE_API + Api.REGISTER)
                .content(new Gson().toJson(userInfo))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        Toast.makeText(RegistActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        RespBase parseObject = new Gson().fromJson(response, RespBase.class);
                        dialog.dismiss();
                        if (parseObject.getCode() == 0) {
                            Toast.makeText(RegistActivity.this, "注册成功请登录", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegistActivity.this, parseObject.getMsg(), Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                });
    }

}
