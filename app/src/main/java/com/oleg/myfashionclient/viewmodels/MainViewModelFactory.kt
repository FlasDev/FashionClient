package com.oleg.myfashionclient.viewmodels

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
/**
 * Created by oleg on 27.03.2018.
 */
class MainViewModelFactory @Inject constructor(application: Application,
                           var firebaseFirestore: FirebaseFirestore,
                           var firebaseAuth: FirebaseAuth
) : ViewModelProvider.AndroidViewModelFactory(application) {

    private var application: Application = application

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application,firebaseFirestore,firebaseAuth) as T
    }
}