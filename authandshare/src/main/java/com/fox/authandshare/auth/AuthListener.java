package com.fox.authandshare.auth;


import com.fox.authandshare.platform.PlatForm;

/**
 * 第三方登录的通用回调
 * @author fox.hu
 * @date 2018/8/16
 */

public interface AuthListener {

    /**
     * @param platform
     */
    void onStart(PlatForm platform);

    /**
     * @param platformInfo
     */
    void onComplete(PlatFormInfo platformInfo);

    /**
     * @param errorMsg
     */
    void onError(String errorMsg);

    /**
     *
     */
    void onCancel();
}
