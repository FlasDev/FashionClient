package com.oleg.myfashionclient.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.util.Log
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.oleg.myfashionclient.common.adapter.MainProductAdapter
import com.oleg.myfashionclient.model.ActionType
import com.oleg.myfashionclient.model.StoreData
import com.oleg.myfashionclient.model.User
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by oleg on 27.03.2018.
 */
interface IAll{
    fun buyProduct(storeData: StoreData?, type: ActionType)
    fun addToBasketProduct(storeData: StoreData?, type: ActionType)
    fun getBalance(): Int
}
interface IAllProducts: IAll {
    fun getAllProductAdapter(owner: LifecycleOwner): MainProductAdapter
}
interface IAvailableProducts: IAll{
    fun getAvailableProductAdapter(owner: LifecycleOwner): MainProductAdapter
}
interface IMain{
    fun addBalance(num: Int)
    fun getName(): String
    val balance: Observable<String?>
}

interface IProduct: IAll{
    val loadData: Observable<StoreData>
    fun loadData(key: String)
}
interface IBasket: IAll{

}
interface  IBuy: IAll{
    fun loadBasketAdapter()
}
class MainViewModel(application: Application,
                    var firebaseFirestore: FirebaseFirestore,
                    var firebaseAuth: FirebaseAuth
) : AndroidViewModel(application), IAllProducts, IAvailableProducts, IAll, IMain, IProduct, IBasket, IBuy {
    override fun loadBasketAdapter() {
        val query = firebaseFirestore.collection("users").document("${firebaseAuth.currentUser?.uid}").get().addOnCompleteListener({
            t->
            val k =  t.result.toObject(User::class.java)
            Log.d("myLogs","$k")
        })
    }

    override val loadData: PublishSubject<StoreData> = PublishSubject.create()
    override val balance: PublishSubject<String?> = PublishSubject.create()
    var bal: Int = 0

    init {
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            bal = documentSnapshot.data["money"].toString().toInt()
            balance.onNext(documentSnapshot.data["money"].toString())
        }
    }

    override fun loadData(key: String) {
        firebaseFirestore.collection("products_2").document(key).addSnapshotListener{ documentSnapshot, firebaseFirestoreException ->
            var storeData = documentSnapshot.toObject(StoreData::class.java)
            Log.d("myLogs","${storeData.key}")
            loadData.onNext(storeData)
        }
    }

    override fun getBalance(): Int {
        return bal
    }

    override fun addBalance(num: Int) {
        var docBal = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)
        firebaseFirestore.runTransaction { transaction ->
            var bal = transaction.get(docBal)

            var myBalance = bal.getLong("money")+num
            transaction.update(docBal,"money",myBalance)
        }
    }

    override fun getName(): String {
        return firebaseAuth.currentUser?.displayName!!
    }

    override fun buyProduct(storeData: StoreData?, type: ActionType) {

        var docProd = firebaseFirestore.collection("products_2").document(storeData?.key!!)

        var docUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)


        firebaseFirestore.runTransaction { transaction: Transaction ->
            var user = transaction.get(docUser)
            var product = transaction.get(docProd)

            var money = user.getLong("money")- storeData.price?.toInt()!!
            var productCount = product.getLong("count")-1
            var userProductMas = user.data.get("personal_effects") as MutableList<String>

            userProductMas.add(storeData.key!!)
            transaction.update(docUser,"money",money)
            transaction.update(docProd,"count",productCount)
            transaction.update(docUser,"personal_effects", userProductMas)
        }.addOnSuccessListener {
            Log.d("myLogs","successs")
        }.addOnFailureListener({
            t-> Log.d("myLogs","${t.message}")
    })
    }

    override fun addToBasketProduct(storeData: StoreData?, type: ActionType) {
        var docUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)

        firebaseFirestore.runTransaction { transaction: Transaction ->
            var user = transaction.get(docUser)

            var userShopCart = user.data.get("shop_cart") as MutableList<String>

            userShopCart.add(storeData?.key!!)
            transaction.update(docUser, "shop_cart", userShopCart)
        }.addOnSuccessListener {
            Log.d("myLogs","successs")
        }.addOnFailureListener({
            t-> Log.d("myLogs","${t.message}")
        }
        )
    }

    override fun getAvailableProductAdapter(owner: LifecycleOwner): MainProductAdapter {
        val query = firebaseFirestore.collection("products_2").whereGreaterThan("count",0)
        val options = FirestoreRecyclerOptions.Builder<StoreData>()
                .setQuery(query, StoreData::class.java)
                .setLifecycleOwner(owner)
                .build()
        return MainProductAdapter(options)
    }

    override fun getAllProductAdapter(owner: LifecycleOwner): MainProductAdapter {
        val query = firebaseFirestore.collection("products_2")
        val options = FirestoreRecyclerOptions.Builder<StoreData>()
                .setQuery(query, StoreData::class.java)
                .setLifecycleOwner(owner)
                .build()
        return MainProductAdapter(options)
    }
}