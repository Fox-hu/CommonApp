package com.fox.authandshare.auth

/**
 * 根据第三方登录 返回的所需信息
 * @author fox.hu
 * @date 2018/8/16
 */

class PlatFormInfo(
    private val id: String,
    private val nickName: String,
    private val gender: String,
    private val iconUrl: String
) {

    override fun toString(): String {
        return "PlatFormInfo{" + "id='" + id + '\''.toString() + ", nickName='" + nickName + '\''.toString() +
                ", gender='" + gender + '\''.toString() + ", iconUrl='" + iconUrl + '\''.toString() + '}'.toString()
    }
}
