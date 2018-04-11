package com.oleg.myfashionclient.common.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.model.StoreData
import com.squareup.picasso.Picasso

class BuyProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){


    fun bindProduct(storeData: StoreData){
        Picasso.get()
                .load(storeData.picture!![0])
                .into(itemView.findViewById<ImageView>(R.id.store_image))

        itemView.findViewById<TextView>(R.id.store_name).text = storeData.name

    }
}