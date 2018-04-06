package com.oleg.myfashionclient.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.ui.BaseFragment
import com.oleg.myfashionclient.viewmodels.IBuy
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import javax.inject.Inject

class BuyFragment : BaseFragment() {
    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm : IBuy by lazy {
        ViewModelProviders.of(this, vmFactory)[MainViewModel::class.java]
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =  inflater.inflate(R.layout.fragment_buy, container, false)
        initUI(view)
        return view
    }

    private fun initUI(view: View) {
        setRecyclerView(view.findViewById(R.id.recycler_view_buy))
    }

    private fun setRecyclerView(recyc: RecyclerView) {
        vm.loadBasketAdapter()
        recyc.layoutManager = LinearLayoutManager(context!!)
    }

    companion object {
        fun newInstance(): BuyFragment{
            return BuyFragment()
        }
    }

}
