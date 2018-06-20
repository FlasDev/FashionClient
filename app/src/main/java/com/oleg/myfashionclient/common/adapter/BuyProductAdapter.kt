package com.oleg.myfashionclient.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.viewholder.BuyProductViewHolder
import com.oleg.myfashionclient.model.NewBuyCart
import com.oleg.myfashionclient.model.ShopCart
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class BuyProductAdapter(options: FirestoreRecyclerOptions<NewBuyCart>) : FirestoreRecyclerAdapter<NewBuyCart, BuyProductViewHolder>(options) {

    val clickSubjectBuy = PublishSubject.create<NewBuyCart>()
    val clickEventBuy: Observable<NewBuyCart> = clickSubjectBuy

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BuyProductViewHolder {
        return BuyProductViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_item_buy,parent,false))
    }

    override fun onBindViewHolder(holder: BuyProductViewHolder, position: Int, model: NewBuyCart) {
        holder.bindProduct(model)
    }
}