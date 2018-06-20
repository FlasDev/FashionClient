package com.oleg.myfashionclient.model

/**
 * Created by oleg on 27.03.2018.
 */
data class StoreData(
        var id: Long? =null,
        var category: String? = null,
        var color: String? = null,
        var name_type: String? = null,
        var currencyId: String? = null,
        var description: String? = null,
        var model: String? = null,
        var name: String? = null,
        var products_type: String? = null,
        var picture: List<String>? = null,
        var price: String? = null,
        var selling_type: String? = null,
        var type: String? = null,
        var count: Int? = null,
        var key: String? = null
)