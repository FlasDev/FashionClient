package com.oleg.myfashionclient.model

import java.util.*

data class ShopCart (
        var id_order: String? = null,
        var id_user: String? = null,
        var id_product: String? = null,
        var date: Date? = null,
        var inBasket: Boolean = false,
        var isBuying: Boolean = false,
        var isDeleted: Boolean = true,
        var picture: String? = null,
        var name: String? = null,
        var price: String? = null
)