package com.oleg.myfashionclient.common.viewholder

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.adapter.BasketProductAdapter
import com.oleg.myfashionclient.model.StoreData
import com.squareup.picasso.Picasso

class BasketProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var adapter: BasketProductAdapter? = null
    var storeData: StoreData? = null

    init {
        itemView?.findViewById<Button>(R.id.action_basket_buy)?.setOnClickListener {
            adapter?.clickSubjectBuy?.onNext(storeData!!)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bindProduct(storeData: StoreData, basketProductAdapter: BasketProductAdapter){
        this.storeData = storeData
        Picasso.get()
                .load(storeData.picture!![0])
                .into(itemView.findViewById<ImageView>(R.id.item_basket_image))

        itemView.findViewById<TextView>(R.id.item_basket_price).text = storeData.price+" "+storeData.currencyId
        itemView.findViewById<TextView>(R.id.item_basket_name).text = storeData.name+" "+storeData.products_type

    }
}