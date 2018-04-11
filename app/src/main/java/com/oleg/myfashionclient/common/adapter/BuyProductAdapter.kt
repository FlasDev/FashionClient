package com.oleg.myfashionclient.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.viewholder.BuyProductViewHolder
import com.oleg.myfashionclient.model.StoreData

class BuyProductAdapter : RecyclerView.Adapter<BuyProductViewHolder>() {

    val product: MutableList<StoreData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BuyProductViewHolder {
        return BuyProductViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_item_buy,parent,false))
    }

    override fun getItemCount(): Int = product.size

    override fun onBindViewHolder(holder: BuyProductViewHolder?, position: Int) {
        holder?.bindProduct(product[position])
    }

    fun addProduct(storeData: StoreData){
        product.add(storeData)
        notifyDataSetChanged()
    }
}