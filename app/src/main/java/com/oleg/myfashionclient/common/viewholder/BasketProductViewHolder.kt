package com.oleg.myfashionclient.common.viewholder

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.adapter.BasketProductAdapter
import com.oleg.myfashionclient.model.ShopCart
import com.squareup.picasso.Picasso

class BasketProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var adapter: BasketProductAdapter? = null
    var store: ShopCart? = null

    init {
        itemView?.findViewById<Button>(R.id.action_basket_buy)?.setOnClickListener {
            Log.d("myLogs","click")
            adapter?.clickSubjectBuy?.onNext(store!!)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bindProduct(store: ShopCart, basketProductAdapter: BasketProductAdapter){
        Log.d("myLogs","$store")
        this.store = store
        this.adapter = basketProductAdapter
        Picasso.get()
                .load(store.picture)
                .into(itemView.findViewById<ImageView>(R.id.item_basket_image))

        itemView.findViewById<TextView>(R.id.item_basket_price).text = store.price
        itemView.findViewById<TextView>(R.id.item_basket_name).text = store.name

    }
}