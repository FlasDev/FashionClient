package com.oleg.myfashionclient.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.model.ActionType
import com.oleg.myfashionclient.model.StoreData
import com.oleg.myfashionclient.ui.BaseFragment
import com.oleg.myfashionclient.ui.activities.main.ProductActivity
import com.oleg.myfashionclient.viewmodels.IAvailableProducts
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import com.trello.rxlifecycle2.android.FragmentEvent
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class AvailableProductsFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm: IAvailableProducts by lazy {
        ViewModelProviders.of(this,vmFactory)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_available_products, container, false)
        initUI(v)
        return v
    }

    private fun initUI(v: View) {
        setRecycler(v.findViewById(R.id.recycler_available_products))
    }

    private fun setRecycler(recycler: RecyclerView) {
        recycler.layoutManager = LinearLayoutManager(context)
        var adapter = vm.getAvailableProductAdapter(this)
        recycler.adapter = adapter

        adapter.clickEventAddBasket
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe({storeData: StoreData? ->
                    displayMessage("Добавленно в корзинку")
                    vm.addToBasketProduct(storeData, ActionType.ADD_TO_BASKET)})

        adapter.clickEventItemView
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe({storeData: StoreData? ->
                    startActivity(ProductActivity.newIntent(context!!,storeData?.key))
                })
    }

    private fun displayMessage(message: String){
        Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(): AvailableProductsFragment{
            return AvailableProductsFragment()
        }
    }
}// Required empty public constructor
