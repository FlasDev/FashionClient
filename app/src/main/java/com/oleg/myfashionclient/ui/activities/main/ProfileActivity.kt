package com.oleg.myfashionclient.ui.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxMenuItem
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
        initUI()
    }

    private fun initUI() {
        setToolbar(toolbar)
        setTextLine()
    }

    private fun setTextLine() {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).addSnapshotListener({
            t1,t2->
            profile_edit_name.setText(t1.data["name"].toString())
            if(t1.data.containsKey("address")) {
                profile_edit_address.setText(t1.data["address"].toString())
            }
            profile_edit_mail.setText(firebaseAuth.currentUser?.email)
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

        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val docUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)
        RxMenuItem.clicks(menu?.findItem(R.id.menu_profile_done)!!)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({
                    Log.d("myLogs","runTransaction")
                    firebaseFirestore.runTransaction({transaction: Transaction ->
                        val user = transaction.get(docUser)
                        val name = profile_edit_name.text
                        val address = profile_edit_address.text
                        Log.d("myLogs","$address")
                        val map = HashMap<String, Any>()
                        map.put("address",address.toString())
                        transaction.update(docUser,"address",address.toString())
                        transaction.update(docUser,"name",name.toString())
                    })
                })
        return true
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            val intent = Intent(packageContext, ProfileActivity::class.java)
            return intent
        }
    }
}
