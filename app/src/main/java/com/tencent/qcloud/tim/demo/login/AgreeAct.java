package com.tencent.qcloud.tim.demo.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;

import static com.tencent.qcloud.tim.uikit.base.ITitleBarLayout.POSITION.MIDDLE;

public class AgreeAct extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agree_activity);
        TitleBarLayout titleBarLayout = findViewById(R.id.title);
        titleBarLayout.setTitle("注册", MIDDLE);
        titleBarLayout.setRightIcon(0);
        titleBarLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
