package com.oleg.myfashionclient.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.viewholder.MainProductViewHolder
import com.oleg.myfashionclient.model.StoreData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by oleg on 27.03.2018.
 */
class MainProductAdapter(options: FirestoreRecyclerOptions<StoreData>) : FirestoreRecyclerAdapter<StoreData, MainProductViewHolder>(options) {

    val clickSubjectAddBasket = PublishSubject.create<StoreData>()
    val clickEventAddBasket: Observable<StoreData> = clickSubjectAddBasket

    val clickSubjectItemView = PublishSubject.create<StoreData>()
    val clickEventItemView: Observable<StoreData> = clickSubjectItemView

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainProductViewHolder {
        return MainProductViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_item,parent,false))
    }

    override fun onBindViewHolder(holder: MainProductViewHolder, position: Int, model: StoreData) {
        holder.bindProduct(model,this)
    }
}