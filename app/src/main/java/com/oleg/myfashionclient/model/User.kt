package com.oleg.myfashionclient.model

/**
 * Created by oleg on 27.03.2018.
 */
data class User(
        var name: String? = null,
        var money: Long? = null,
        var mobile: String? = null,
        var address: String? = null,
        var shop_cart: List<String>? = null,
        var personal_effects: List<String>? = null
)