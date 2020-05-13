package com.fox.toutiao.ui.setting

import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentSettingBinding
import com.silver.fox.base.component.ui.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingFragment : BaseVMFragment<SettingViewModel, FragmentSettingBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_setting
    override fun bindDataAndEvent() {
    }

    override val viewModel: SettingViewModel by viewModel()

}
