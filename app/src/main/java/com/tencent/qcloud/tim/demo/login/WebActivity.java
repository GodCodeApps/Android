package com.tencent.qcloud.tim.demo.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.demo.R;

public class WebActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        webView = findViewById(R.id.web);
        loadWebView(getIntent().getStringExtra("url"));
    }

    void loadWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM Storage
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setDownloadListener(new MyWebViewDownLoadListener());

    }

    class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
