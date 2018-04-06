package com.oleg.myfashionclient.ui.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.adapter.ViewPagerAdapter
import com.oleg.myfashionclient.ui.BaseActivity
import com.oleg.myfashionclient.ui.fragments.BasketFragment
import com.oleg.myfashionclient.ui.fragments.BuyFragment
import com.trello.rxlifecycle2.android.ActivityEvent
import kotlinx.android.synthetic.main.app_bar_basket.*

class BasketActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_basket)
        initUI()
        Log.d("myLogs","BasketActivity")
    }

    private fun initUI() {
        setToolbar(toolbar)
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
        adapter.addFragment(BuyFragment.newInstance(),"Покупка")
        viewpager?.adapter = adapter
        tabs.setupWithViewPager(viewpager)

    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            var intent: Intent = Intent(packageContext, BasketActivity::class.java)
            return intent
        }
    }
}
