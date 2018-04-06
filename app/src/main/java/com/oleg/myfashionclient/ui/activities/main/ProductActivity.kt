package com.oleg.myfashionclient.ui.activities.main

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.model.ActionType
import com.oleg.myfashionclient.model.StoreData
import com.oleg.myfashionclient.ui.BaseActivity
import com.oleg.myfashionclient.viewmodels.IProduct
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.app_bar_product.*
import javax.inject.Inject

class ProductActivity : BaseActivity() {

    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm: IProduct by lazy {
        ViewModelProviders.of(this,vmFactory)[MainViewModel::class.java]
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_product)
        var key = intent.getSerializableExtra(EXTRA_PRODUCT_ID) as String
        vm.loadData(key)
        vm.loadData.subscribe({storeData->
            setToolbar(toolbar,storeData)
            setFloatButton(product_fab,storeData)
            Picasso.get()
                    .load(storeData.picture!![0])
                    .into(product_image)

            product_name.text = "${storeData.name} ${storeData.products_type}"
            product_model.text = "Модель: "+storeData.model
            product_color.text = "Цвет: "+storeData.color?.trim()
            product_description.text = storeData.description
        })
    }

    private fun setFloatButton(product_fab: FloatingActionButton,storeData: StoreData) {
        RxView.clicks(product_fab)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({vm.addToBasketProduct(storeData,ActionType.ADD_TO_BASKET)})
    }

    private fun setToolbar(toolbar: Toolbar?, storeData: StoreData) {
        toolbar?.title = storeData.name
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        RxToolbar.navigationClicks(toolbar!!)
                .compose(bindUntilEvent<Any>(ActivityEvent.DESTROY))
                .subscribe({onBackPressed()})
    }

    companion object {

        private const val EXTRA_PRODUCT_ID: String = "com.oleg.product_key"

        fun newIntent(context: Context, key: String?): Intent {
            val intent = Intent(context,ProductActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, key)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            return intent

        }
    }
}
