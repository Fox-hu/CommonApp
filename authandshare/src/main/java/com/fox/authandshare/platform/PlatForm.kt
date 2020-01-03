package com.fox.authandshare.platform


import com.fox.authandshare.auth.sina.Constants

/**
 * 第三方登录的平台
 * @author fox.hu
 * @date 2018/8/16
 */

enum class PlatForm private constructor(
    val showWord: String,
    val appId: String,
    val appKey: String,
    val scope: String
) {
    QQ(PlatformName.QQ, "100424468", "", ""),
    WEIXIN(PlatformName.WEIXIN, "wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0", ""),
    ALIPAY(PlatformName.ALIPAY, "", "", ""),
    SINA(PlatformName.SINA, "2200713412", "http://sns.whalecloud.com", Constants.SCOPE)

}
