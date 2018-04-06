package com.oleg.myfashionclient.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.ui.BaseFragment


class BasketFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    companion object {
        fun newInstance(): BasketFragment{
            return BasketFragment()
        }
    }

}
