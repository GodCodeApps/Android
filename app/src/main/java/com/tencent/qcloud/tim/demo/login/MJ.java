package com.tencent.qcloud.tim.demo.login;

public class MJ {

    /**
     * success : true
     * ShowWeb : 1
     * PushKey :
     * Url : http://152.cc
     */

    private String success;
    private String ShowWeb;
    private String PushKey;
    private String appId;
    private String status;
    private String url;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getShowWeb() {
        return ShowWeb;
    }

    public void setShowWeb(String ShowWeb) {
        this.ShowWeb = ShowWeb;
    }

    public String getPushKey() {
        return PushKey;
    }

    public void setPushKey(String PushKey) {
        this.PushKey = PushKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String Url) {
        this.url = Url;
    }
}
