package com.silver.fox.stateslayout

import android.view.View

/**
 * @author francis.fan 2018/7/18
 * !!!注意 State 和 States是不一样的哦!!!
 *
 *
 * 内部接口，
 * 不同状态的页面均需要实现该接口
 * 不同状态的内部细节管理由自身管理，StatesLayout仅仅用于状态的切换
 */
interface StateLayout {
    fun getStateView(): View

    fun bindData(stateParam: StateParam)
}