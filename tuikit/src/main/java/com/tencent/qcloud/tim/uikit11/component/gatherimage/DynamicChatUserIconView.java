package com.tencent.qcloud.tim.uikit11.component.gatherimage;

import com.tencent.qcloud.tim.uikit11.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit11.utils.ScreenUtil;

public abstract class DynamicChatUserIconView extends DynamicLayoutView<MessageInfo> {

    private int iconRadius = -1;

    public int getIconRadius() {
        return iconRadius;
    }

    /**
     * 设置聊天头像圆角
     *
     * @param iconRadius
     */
    public void setIconRadius(int iconRadius) {
        this.iconRadius = ScreenUtil.getPxByDp(iconRadius);
    }
}
