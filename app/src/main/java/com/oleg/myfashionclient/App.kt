package com.oleg.myfashionclient

import android.app.Activity
import android.app.Application
import com.oleg.myfashionclient.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by oleg on 27.03.2018.
 */
class App: Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}