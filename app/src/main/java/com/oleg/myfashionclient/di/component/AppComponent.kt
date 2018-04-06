package com.oleg.myfashionclient.di.component

import android.app.Application
import com.oleg.myfashionclient.App
import com.oleg.myfashionclient.di.module.ActivityBuilder
import com.oleg.myfashionclient.di.module.AppModule
import com.oleg.myfashionclient.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by oleg on 27.03.2018.
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class, ActivityBuilder::class, ViewModelModule::class, AppModule::class])
interface AppComponent: AndroidInjector<App> {
    @Component.Builder
    interface Builder{
        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }
}