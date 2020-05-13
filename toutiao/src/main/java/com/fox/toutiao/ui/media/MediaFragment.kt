package com.fox.toutiao.ui.media

import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentPhotoBinding
import com.silver.fox.base.component.ui.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment : BaseVMFragment<MediaViewModel, FragmentPhotoBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_media
    override fun bindDataAndEvent() {

    }

    override val viewModel: MediaViewModel by viewModel()
}