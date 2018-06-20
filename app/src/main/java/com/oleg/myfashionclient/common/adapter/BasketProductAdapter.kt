package com.oleg.myfashionclient.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.viewholder.BasketProductViewHolder
import com.oleg.myfashionclient.model.ShopCart
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BasketProductAdapter(options: FirestoreRecyclerOptions<ShopCart>) : FirestoreRecyclerAdapter<ShopCart, BasketProductViewHolder>(options) {

    val clickSubjectBuy = PublishSubject.create<ShopCart>()
    val clickEventBuy: Observable<ShopCart> = clickSubjectBuy

    val clickSubjectDelete = PublishSubject.create<ShopCart>()
    val clickEventDelete: Observable<ShopCart> = clickSubjectDelete

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BasketProductViewHolder {
        return BasketProductViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_item_basket,parent,false))
    }

    override fun onBindViewHolder(holder: BasketProductViewHolder, position: Int, model: ShopCart) {
        holder.bindProduct(model,this)
    }

    override fun getItemCount(): Int {
            return super.getItemCount()
    }
}