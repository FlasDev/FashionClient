package com.oleg.myfashionclient.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.ui.BaseFragment
import com.oleg.myfashionclient.viewmodels.IBasket
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import javax.inject.Inject


class BasketFragment : BaseFragment() {
    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm : IBasket by lazy {
        ViewModelProviders.of(this, vmFactory)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_basket, container, false)
        initUI(view)
        return view
    }

    private fun initUI(view: View) {
        setRecyclerView(view.findViewById<RecyclerView>(R.id.recycler_view_basket))
    }

    private fun setRecyclerView(recycler: RecyclerView) {
        recycler.layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(context!!,HORIZONTAL)
        val adapter = vm.getBasketAdapter(this)
        recycler.addItemDecoration(divider)
        recycler.adapter = adapter
        adapter.clickEventBuy.subscribe({

            store->
            Log.d("myLogs","buy click")
            vm.addToBuy(store)
        })
    }

    companion object {
        fun newInstance(): BasketFragment{
            return BasketFragment()
        }
    }

}
