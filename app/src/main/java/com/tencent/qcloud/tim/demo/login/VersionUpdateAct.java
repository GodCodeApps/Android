package com.tencent.qcloud.tim.demo.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.demo.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

public class VersionUpdateAct extends Activity {

    private Uri apkUri;
    private TextView tvpoint;
    private SeekBar pB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_update_activity);
        tvpoint = findViewById(R.id.tv_point);
        pB = findViewById(R.id.pb);
        String url = getIntent().getStringExtra("url");
        fileDownload(UUID.randomUUID().toString().replace("-", ""), url);
    }

    private void installApk(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                apkUri = FileProvider.getUriForFile(
                        this,
                        this.getPackageName() + ".provider",
                        file
                );
            } else {
                apkUri = Uri.fromFile(file);
            }
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            // 查询所有符合 intent 跳转目标应用类型的应用，注意此方法必须放置在 setDataAndType 方法之后
            List<ResolveInfo> resolveLists = getPackageManager().queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
            );
            // 然后全部授权
            for (int i = 0; i < resolveLists.size(); i++) {
                String packageName = resolveLists.get(i).activityInfo.packageName;
                grantUriPermission(
                        packageName,
                        apkUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
            }
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void fileDownload(String name, String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(Util.getCacheDir(this).getAbsolutePath(),
                        name + ".temp") {
                    @Override
                    public void inProgress(final float progress, long total, int id) {
                        final int b1 = (int) (progress * 100);
                        if (b1 > b2) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    tvpoint.setText(b1+"");
                                    pB.setProgress(b1);
                                    b2 = (int) (progress * 100);
                                }
                            });
                        }
                    }

                    int b2 = 0;

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        installApk(response);
                    }
                });
    }
}
