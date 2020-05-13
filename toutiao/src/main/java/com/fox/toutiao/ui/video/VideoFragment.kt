package com.fox.toutiao.ui.video

import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentVideoBinding
import com.silver.fox.base.component.ui.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class VideoFragment : BaseVMFragment<VideoViewModel, FragmentVideoBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_video
    override fun bindDataAndEvent() {

    }

    override val viewModel: VideoViewModel by viewModel()
}
