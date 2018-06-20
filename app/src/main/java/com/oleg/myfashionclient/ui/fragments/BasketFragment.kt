package com.oleg.myfashionclient.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.ui.BaseFragment
import com.oleg.myfashionclient.ui.view.hide
import com.oleg.myfashionclient.ui.view.show
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
        setRecyclerView(view.findViewById<RecyclerView>(R.id.recycler_view_basket),view)
    }

    private fun setRecyclerView(recycler: RecyclerView, view: View) {
        recycler.layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(context!!,HORIZONTAL)
        val adapter = vm.getBasketAdapter(this)
        recycler.addItemDecoration(divider)
        recycler.adapter = adapter
        //Log.d("myLogs","itemCount = ${adapter.itemCount}")
        if (recycler.adapter.itemCount == 0){
            view.findViewById<TextView>(R.id.text_clear_basket).show()

        }else{
            view.findViewById<TextView>(R.id.text_clear_basket).hide()
        }

        adapter.clickEventBuy.subscribe({
            shopCart-> vm.madeOrder(shopCart)
        })
        adapter.clickEventDelete.subscribe{
            shopCart->
            vm.deleteFromBasket(shopCart)
        }
    }

    companion object {
        fun newInstance(): BasketFragment{
            return BasketFragment()
        }
    }

}
