package com.tencent.qcloud.tim.uikit11.component.video.listener;

import android.graphics.Bitmap;

public interface JCameraListener {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame, long duration);

}
