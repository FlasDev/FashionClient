package com.oleg.myfashionclient.common.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.common.adapter.MainProductAdapter
import com.oleg.myfashionclient.model.StoreData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*

/**
 * Created by oleg on 27.03.2018.
 */
class MainProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private var mainProductAdapter: MainProductAdapter? = null
    private var storeData: StoreData? = null
    init {
        var buttonBuy = itemView?.findViewById<Button>(R.id.store_action_buy)
        buttonBuy?.setOnClickListener {
            mainProductAdapter?.clickSubjectBuy?.onNext(storeData!!)
        }

        var buttonAddToBasket = itemView?.findViewById<Button>(R.id.store_action_add_to_basket)
        buttonAddToBasket?.setOnClickListener {
            mainProductAdapter?.clickSubjectAddBasket?.onNext(storeData!!)
        }

        itemView?.setOnClickListener({
            mainProductAdapter?.clickSubjectItemView?.onNext(storeData!!)
        })
    }
    @SuppressLint("SetTextI18n")
    fun bindProduct(storeData: StoreData, mainProductAdapter: MainProductAdapter){
        this.mainProductAdapter = mainProductAdapter
        this.storeData = storeData
        itemView.store_name.text = storeData.name?.plus(" ${storeData.products_type}")
        Picasso.get()
                .load(storeData.picture?.get(0))
                .into(itemView.store_image)

        itemView.store_price.text = "Цена: ${storeData?.price} ${storeData?.currencyId}"
        if (storeData.count == 0) {
            itemView.store_count.text = "Нету в наличии"
            itemView.store_count.setTextColor(Color.RED)
        } else {
            itemView.store_count.text = "Есть в наличии ${storeData.count}"
            itemView.store_count.setTextColor(Color.GREEN)
        }
    }
}