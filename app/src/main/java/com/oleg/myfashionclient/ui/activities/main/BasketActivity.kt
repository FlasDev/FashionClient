package com.oleg.myfashionclient.ui.activities.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.adapter.ViewPagerAdapter
import com.oleg.myfashionclient.ui.BaseActivity
import com.oleg.myfashionclient.ui.fragments.BasketFragment
import com.oleg.myfashionclient.ui.fragments.BuyFragment
import com.oleg.myfashionclient.viewmodels.IAll
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import javax.inject.Inject

class BasketActivity : BaseActivity() {

    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm : IAll by lazy {
        ViewModelProviders.of(this, vmFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_basket)
        initUI()
        Log.d("myLogs","BasketActivity")
    }

    private fun initUI() {

        setToolbar(findViewById(R.id.toolbar))
        setViewPager(findViewById(R.id.viewpager))
    }

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Корзинка"
        if(supportActionBar !=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        RxToolbar.navigationClicks(toolbar)
                .compose(bindUntilEvent<Any>(ActivityEvent.DESTROY))
                .subscribe({onBackPressed()})
    }

    private fun setViewPager(viewpager: ViewPager?) {
        var adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(BasketFragment.newInstance(),"Корзинка")
        adapter.addFragment(BuyFragment.newInstance(),"История покупок")
        viewpager?.adapter = adapter
        findViewById<TabLayout>(R.id.tabs).setupWithViewPager(viewpager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_basket,menu)
        RxMenuItem.clicks(menu?.findItem(R.id.menu_basket_delete_all_bakset)!!)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({
                    vm.clearAllBasket()
                })
        return true
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            var intent: Intent = Intent(packageContext, BasketActivity::class.java)
            return intent
        }
    }
}
