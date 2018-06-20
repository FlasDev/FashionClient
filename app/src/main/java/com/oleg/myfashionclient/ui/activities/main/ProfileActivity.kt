package com.oleg.myfashionclient.ui.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.ui.BaseActivity
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.app_bar_profile.*
import javax.inject.Inject

class ProfileActivity : BaseActivity() {

    @Inject
    lateinit var vmFactory: MainViewModelFactory



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_profile)
        setClick(false)
        initUI()
    }

    private fun initUI() {
        setToolbar(toolbar)
        setTextLine()
        setEditButton(profile_edit)
    }

    private fun setEditButton(imageView: ImageView) {
        RxView.clicks(imageView)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({
                    setClick(true)
                })
    }

    private fun save(){
        setClick(false)
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val mas = mutableMapOf<String, Any>()
        mas.put("name",profile_name.text.toString())
        mas.put("address",profile_address.text.toString())
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).update(mas)
    }

    private fun setClick(boolean: Boolean)
    {
        profile_address.isEnabled = boolean
        profile_address.isClickable = boolean
        profile_address.isCursorVisible = boolean

        profile_mobile.isClickable = boolean
        profile_mobile.isCursorVisible = boolean
        profile_mobile.isEnabled = boolean

    }

    private fun setTextLine() {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).addSnapshotListener({
            t1,t2->
            profile_name.text = t1.data["name"].toString()
            if(t1.data.containsKey("address")) {
               profile_address.setText(t1.data["address"].toString())
            }
            if(t1.data.containsKey("mobile")){
                profile_mobile.setText(t1.data["mobile"].toString())
            }
            profile_email.setText(firebaseAuth.currentUser?.email)
        })
    }

    private fun setToolbar(toolbar: Toolbar?) {
        toolbar?.title = "Профиль"
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        RxToolbar.navigationClicks(toolbar!!)
                .compose(bindUntilEvent<Any>(ActivityEvent.DESTROY))
                .subscribe({onBackPressed()})
    }

   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile,menu)
        RxMenuItem.clicks(menu?.findItem(R.id.menu_profile_done)!!)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({save()
                })
        return true
    }

    companion object {
        val firestClick = "One"
        val secondClick = "Two"
        fun newIntent(packageContext: Context): Intent {
            val intent = Intent(packageContext, ProfileActivity::class.java)
            return intent
        }
    }
}
