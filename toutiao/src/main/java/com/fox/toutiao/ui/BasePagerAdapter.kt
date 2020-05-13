package com.fox.toutiao.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter


/**
 * Created by fox on 2018/1/28.
 */
class BasePagerAdapter(fm: FragmentManager, var titleList: List<String>,
                       var fragmentList: List<Fragment>) : FragmentStatePagerAdapter(fm) {

    companion object {
        private val TAG = BasePagerAdapter::class.java.simpleName
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return if (titleList.isEmpty()) 0 else titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}