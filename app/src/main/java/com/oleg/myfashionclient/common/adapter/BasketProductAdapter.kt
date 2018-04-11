package com.oleg.myfashionclient.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.viewholder.BasketProductViewHolder
import com.oleg.myfashionclient.model.StoreData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BasketProductAdapter: RecyclerView.Adapter<BasketProductViewHolder>() {
    val product: MutableList<StoreData> = mutableListOf()

    val clickSubjectBuy = PublishSubject.create<StoreData>()
    val clickEventBuy: Observable<StoreData> = clickSubjectBuy

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BasketProductViewHolder {
        return BasketProductViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_item_basket,parent,false))
    }

    override fun getItemCount(): Int =  product.size

    override fun onBindViewHolder(holder: BasketProductViewHolder?, position: Int) {
        holder?.bindProduct(product[position],this)
    }

    fun addBasketProduct(storeData: StoreData){
        product.add(storeData)
        notifyDataSetChanged()
    }
}