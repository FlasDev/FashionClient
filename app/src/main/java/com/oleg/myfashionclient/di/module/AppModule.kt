package com.oleg.myfashionclient.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by oleg on 27.03.2018.
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provide(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}