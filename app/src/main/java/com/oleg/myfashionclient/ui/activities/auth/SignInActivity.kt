package com.oleg.myfashionclient.ui.activities.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.rxbinding2.view.RxView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.model.User
import com.oleg.myfashionclient.ui.BaseActivity
import com.oleg.myfashionclient.ui.activities.main.MainActivity
import com.trello.rxlifecycle2.android.ActivityEvent.DESTROY
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {
    private val PATH_TOS = ""
    private val RC_SIGN_IN = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        var auth = FirebaseAuth.getInstance().currentUser
        if(auth!=null){

           FirebaseFirestore.getInstance().collection("users").document(auth.uid).get().addOnCompleteListener {
               task ->
               if(!task.result.exists()){
                   var user = User(auth.displayName,0)
                   FirebaseFirestore.getInstance().collection("users").document(auth.uid).set(user)
               }
           }
            loginUser()
        }
        initUI()
    }

    private fun initUI() {
        initLoginButton(login_button)
    }

    private fun initLoginButton(login_button: Button) {
        RxView.clicks(login_button)
                .compose(bindUntilEvent(DESTROY))
                .subscribe({
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                            .setTosUrl(PATH_TOS)
                            .build(),RC_SIGN_IN)
                })
    }

    private fun loginUser(){
        startActivity(MainActivity.newIntent(baseContext))
    }

        private fun displayMessage(message: String){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == Activity.RESULT_OK){
                loginUser()
            }
            if(resultCode == Activity.RESULT_CANCELED){
                displayMessage(getString(R.string.sign_in_failed))
            }
            return
        }
        displayMessage(getString(R.string.unknown_response))
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            var intent: Intent = Intent(packageContext, SignInActivity::class.java)
            return intent
        }
    }
}
