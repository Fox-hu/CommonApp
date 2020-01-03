package com.fox.authandshare.platform;


import com.fox.authandshare.auth.sina.Constants;

/**
 * 第三方登录的平台
 * @author fox.hu
 * @date 2018/8/16
 */

public enum PlatForm {
    QQ(PlatformName.QQ, "100424468", "", ""),
    WEIXIN(PlatformName.WEIXIN, "wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0", ""),
    ALIPAY(PlatformName.ALIPAY, "", "", ""),
    SINA(PlatformName.SINA, "2200713412", "http://sns.whalecloud.com", Constants.SCOPE);

    private String keyWord;
    private String appId;
    private String appKey;
    private String scope;

    PlatForm(String keyWord, String appId, String appKey, String scope) {
        this.scope = scope;
        this.keyWord = keyWord;
        this.appId = appId;
        this.appKey = appKey;
    }

    public String getScope() {
        return scope;
    }

    public String getShowWord() {
        return keyWord;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

}
