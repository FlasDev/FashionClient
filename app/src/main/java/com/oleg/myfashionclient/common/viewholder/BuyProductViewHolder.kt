package com.oleg.myfashionclient.common.viewholder

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.model.NewBuyCart
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item_buy.view.*


class BuyProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){


    @SuppressLint("SetTextI18n")
    fun bindProduct(buyCart: NewBuyCart){
        itemView.store_date.text = "Дата одобрения заказа "+ buyCart.date?.toString()
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val docProd = firebaseFirestore.collection("products_2").document("${buyCart.id_product}")

        docProd.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            val pictureList = task.result["picture"] as List<String>
            val picture = pictureList[0]
            Picasso.get()
                    .load(picture)
                    .into(itemView.findViewById<ImageView>(R.id.store_image))
            itemView.store_name.text = task.result["name_type"].toString()
            itemView.store_price.text = task.result["price"].toString()+" UAH"

        }
    }
}