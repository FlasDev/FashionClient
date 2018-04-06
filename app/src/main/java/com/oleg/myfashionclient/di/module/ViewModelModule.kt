package com.oleg.myfashionclient.di.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by oleg on 27.03.2018.
 */
@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideMainVM(application: Application,firebaseFirestore: FirebaseFirestore, firebaseAuth: FirebaseAuth): ViewModelProvider.AndroidViewModelFactory{
        return MainViewModelFactory(application,firebaseFirestore, firebaseAuth)
    }
}