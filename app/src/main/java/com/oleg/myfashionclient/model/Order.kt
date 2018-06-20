package com.oleg.myfashionclient.model

import java.util.*

data class Order(
        var id_order: String?,
        var order_time: Date,
        var id_user: String?,
        var id_shopCart: String?,
        var id_product: String?,
        var wait: Boolean = true,
        var accept: Boolean = false
)