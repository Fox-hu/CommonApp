package com.fox.toutiao.ui.photo

import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentPhotoBinding
import com.silver.fox.base.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoFragment : BaseVMFragment<PhotoViewModel, FragmentPhotoBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_photo
    override fun bindDataAndEvent() {

    }

    override val viewModel: PhotoViewModel by viewModel()
}