package com.oleg.myfashionclient.di.module

import com.oleg.myfashionclient.ui.activities.auth.SignInActivity
import com.oleg.myfashionclient.ui.activities.main.BasketActivity
import com.oleg.myfashionclient.ui.activities.main.MainActivity
import com.oleg.myfashionclient.ui.activities.main.ProductActivity
import com.oleg.myfashionclient.ui.activities.main.ProfileActivity
import com.oleg.myfashionclient.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by oleg on 27.03.2018.
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun provideSignInActivity(): SignInActivity

    @ContributesAndroidInjector
    abstract fun provideBasketActivity(): BasketActivity

    @ContributesAndroidInjector
    abstract fun provideAllProductsFragment(): AllProductsFragment

    @ContributesAndroidInjector
    abstract fun provideAvailableProductsFragment(): AvailableProductsFragment

    @ContributesAndroidInjector
    abstract fun provideProductActivity(): ProductActivity

    @ContributesAndroidInjector
    abstract fun provideBuyFragment(): BuyFragment

    @ContributesAndroidInjector
    abstract fun provideBasketFragment(): BasketFragment

    @ContributesAndroidInjector
    abstract fun provideProfileActivity(): ProfileActivity

    @ContributesAndroidInjector
    abstract fun provideSearchFragment(): SearchFragment

}