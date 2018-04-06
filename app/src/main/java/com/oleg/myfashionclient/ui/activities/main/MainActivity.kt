package com.oleg.myfashionclient.ui.activities.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.adapter.ViewPagerAdapter
import com.oleg.myfashionclient.ui.BaseActivity
import com.oleg.myfashionclient.ui.activities.auth.SignInActivity
import com.oleg.myfashionclient.ui.fragments.AllProductsFragment
import com.oleg.myfashionclient.ui.fragments.AvailableProductsFragment
import com.oleg.myfashionclient.ui.view.showBalanceDialog
import com.oleg.myfashionclient.viewmodels.IMain
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm : IMain by lazy {
        ViewModelProviders.of(this, vmFactory)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var auth = FirebaseAuth.getInstance().currentUser
        if (auth == null) {
            signOut()
        }
        initUI()
    }

    private fun initUI() {
        setNavigations(nav_view)
        setToolbar(toolbar)
        setViewPager(viewpager)
        setNavHeader(nav_view)
        setBalanceButton(nav_view.getHeaderView(0).findViewById<Button>(R.id.action_add_balance))
    }

    private fun setBalanceButton(action_add_balance: Button) {

        RxView.clicks(action_add_balance)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showBalanceDialog(this)
                            .compose(bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe({t: String? ->
                                Log.d("myLogs","тут")
                                var num = t?.replace(" UAH","")
                                var nam = num?.toInt()
                                vm.addBalance(nam!!)
                            })
                })
    }

    private fun setNavHeader(nav_view: NavigationView) {
        updateName()
        updateBalance()
    }
    private fun updateName(){
        nav_view.getHeaderView(0).findViewById<TextView>(R.id.header_drawer_name).text = vm.getName()

    }
    private fun updateBalance(){
        vm.balance.subscribe({t ->  nav_view.getHeaderView(0).findViewById<TextView>(R.id.header_drawer_balance).text = getString(R.string.subtitle_money,t)})
    }
    
    private fun setViewPager(viewpager: ViewPager) {
        var adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AllProductsFragment.newInstance(),"Все")
        adapter.addFragment(AvailableProductsFragment.newInstance(),"В Наличии")
        viewpager.adapter = adapter

        tabs.setupWithViewPager(viewpager)
    }

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Магазин"
        if(supportActionBar !=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        RxToolbar.navigationClicks(toolbar)
                .compose(bindUntilEvent<Any>(ActivityEvent.DESTROY))
                .subscribe({openDrawer(true) })
    }

    private fun setNavigations(nav_view: NavigationView) {
        RxNavigationView.itemSelections(nav_view)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .doOnNext({openDrawer(false)})
                .subscribe({item: MenuItem? ->
                    when(item?.itemId){
                        R.id.nav_draw_exit ->
                            AuthUI.getInstance()
                                    .signOut(this)
                                    .addOnCompleteListener {
                                        task ->
                                        if(task.isSuccessful){
                                            signOut()
                                        }
                                    }

                        R.id.nav_draw_store_basket-> startActivity(BasketActivity.newIntent(this))

                    }
                })


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun openDrawer(isDrawerOpen: Boolean) {
        if (isDrawerOpen) {
            drawer_layout.openDrawer(GravityCompat.START)
        } else {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun signOut() {
        startActivity(SignInActivity.newIntent(this))
    }
    companion object {
        fun newIntent(packageContext: Context): Intent{
            var intent: Intent = Intent(packageContext, MainActivity::class.java)
            return intent
        }
    }
}
