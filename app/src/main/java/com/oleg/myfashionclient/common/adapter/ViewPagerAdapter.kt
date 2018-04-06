package com.oleg.myfashionclient.common.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by oleg on 27.03.2018.
 */
class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = mFragmentTitleList[position]

    fun addFragment(fragment: Fragment, title: String){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}