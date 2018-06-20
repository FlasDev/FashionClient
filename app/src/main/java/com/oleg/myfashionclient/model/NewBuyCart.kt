package com.oleg.myfashionclient.model

import java.util.*

data class NewBuyCart(
        var id_buyCart: String? = null,
        var id_order: String? = null,
        var id_product: String? = null,
        var date: Date? = null,
        var id_user: String? = null
)