package com.oleg.myfashionclient.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.util.Log
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.oleg.myfashionclient.common.adapter.BasketProductAdapter
import com.oleg.myfashionclient.common.adapter.BuyProductAdapter
import com.oleg.myfashionclient.common.adapter.MainProductAdapter
import com.oleg.myfashionclient.model.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created by oleg on 27.03.2018.
 */
interface IAll{
    //fun buyProduct(order: Order)
    fun addToBasketProduct(storeData: StoreData?, type: ActionType)
    fun getBalance(): Int
    fun clearAllBasket()
}
interface IAllProducts: IAll {
    fun getAllProductAdapter(owner: LifecycleOwner): MainProductAdapter
}
interface IAvailableProducts: IAll{
    fun getAvailableProductAdapter(owner: LifecycleOwner): MainProductAdapter
}
interface IMain: IAll{
    fun addBalance(num: Int)
    fun getName(): String
    val balance: Observable<String?>
    fun sortPriceAZ(owner: LifecycleOwner): MainProductAdapter
    fun sortAmountAZ(owner: LifecycleOwner): MainProductAdapter
    fun sortPriceZA(owner: LifecycleOwner): MainProductAdapter
    fun sortAmountZA(owner: LifecycleOwner): MainProductAdapter
  //  fun loadSearchAdapter(owner: LifecycleOwner): MainProductAdapter
}

interface IProduct: IAll{
    val loadData: Observable<StoreData>
    fun loadData(key: String)
}
interface IBasket: IAll{
    fun loadBasketAdapter(adapter: BasketProductAdapter)
    fun getBasketAdapter(owner: LifecycleOwner): BasketProductAdapter
    fun addToBuy(shopCart: ShopCart)
    fun madeOrder(shopCart: ShopCart)
    fun deleteFromBasket(shopCart: ShopCart?)
}
interface IBuy: IAll{
    fun loadBuyAdapter(adapter: BuyProductAdapter)
    fun getBuyAdapter(owner: LifecycleOwner): BuyProductAdapter

}

interface IProfile{

}
class MainViewModel(application: Application,
                    var firebaseFirestore: FirebaseFirestore,
                    var firebaseAuth: FirebaseAuth
) : AndroidViewModel(application), IAllProducts, IAvailableProducts, IAll, IMain, IProduct, IBasket, IBuy,IProfile {
    override fun sortPriceZA(owner: LifecycleOwner): MainProductAdapter {
        val query = firebaseFirestore.collection("products_2")
                .orderBy("price",com.google.firebase.firestore.Query.Direction.DESCENDING)


        val options = FirestoreRecyclerOptions.Builder<StoreData>()
                .setQuery(query, StoreData::class.java)
                .setLifecycleOwner(owner)
                .build()

        return MainProductAdapter(options)
    }

    override fun sortAmountZA(owner: LifecycleOwner): MainProductAdapter {
        val query = firebaseFirestore.collection("products_2")
                .orderBy("count",com.google.firebase.firestore.Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<StoreData>()
                .setQuery(query, StoreData::class.java)
                .setLifecycleOwner(owner)
                .build()

        return MainProductAdapter(options)
    }

    override fun sortPriceAZ(owner: LifecycleOwner): MainProductAdapter {
        val query = firebaseFirestore.collection("products_2")
                .orderBy("price",com.google.firebase.firestore.Query.Direction.ASCENDING)


        val options = FirestoreRecyclerOptions.Builder<StoreData>()
                .setQuery(query, StoreData::class.java)
                .setLifecycleOwner(owner)
                .build()

        return MainProductAdapter(options)
    }

    override fun sortAmountAZ(owner: LifecycleOwner): MainProductAdapter {
        val query = firebaseFirestore.collection("products_2")
                .orderBy("count",com.google.firebase.firestore.Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<StoreData>()
                .setQuery(query, StoreData::class.java)
                .setLifecycleOwner(owner)
                .build()

        return MainProductAdapter(options)
    }




    override fun clearAllBasket() {

        val doc = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("shop_cart").get()
        doc.addOnCompleteListener {
            task: Task<QuerySnapshot> ->
            for(doc in task.result.documents){
                doc.reference.delete()
            }
        }
    }

    override fun deleteFromBasket(shopCart: ShopCart?) {
       firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("shop_cart").document(shopCart?.id_order!!).delete()
    }


    override fun addToBuy(shopCart: ShopCart) {
        val docShop = firebaseFirestore.collection("shop_cart").document(shopCart.id_order!!)
        val docProd = firebaseFirestore.collection("products_2").document(shopCart.id_product!!)
        val docUser = firebaseFirestore.collection("users").document(shopCart.id_user!!)
        firebaseFirestore.runTransaction { transaction: Transaction ->
            val user = transaction.get(docUser)
            val product = transaction.get(docProd)
            var money = user.getLong("money")- shopCart.price?.split(" ")!![0].toInt()
            var productCount = product.getLong("count")-1
            transaction.update(docUser,"money",money)
            transaction.update(docProd,"count",productCount)

            firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("buying").add(shopCart)
        }
    }

    override fun loadBasketAdapter(adapter: BasketProductAdapter) {
        firebaseFirestore.collection("shop_cart").whereEqualTo("id_user",firebaseAuth.currentUser?.uid).addSnapshotListener({
            t1,t2->
        })
        firebaseFirestore.collection("users").document("${firebaseAuth.currentUser?.uid}").get().addOnCompleteListener({
            t->
            val k =  t.result.toObject(User::class.java)
            var mas = k.shop_cart
            for(i: Int in mas?.indices!!){
                firebaseFirestore.collection("products_2").document(mas[i]).get().addOnCompleteListener {
                    task ->
                    var data = task.result.toObject(StoreData::class.java)
                    //adapter.addBasketProduct(data)
                }
            }
        })
    }

    override fun loadBuyAdapter(adapter: BuyProductAdapter) {
        val query = firebaseFirestore.collection("users").document("${firebaseAuth.currentUser?.uid}").get().addOnCompleteListener({
            t->
            val k =  t.result.toObject(User::class.java)
            var mas = k.personal_effects
            for(i: Int in mas?.indices!!){
                firebaseFirestore.collection("products_2").document(mas[i]).get().addOnCompleteListener {
                    task ->
                    var data = task.result.toObject(StoreData::class.java)
                    //adapter.addProduct(data)
                }
            }
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
        var name ="null"

        return name

    }

    override fun madeOrder(shopCart: ShopCart) {
        val docShopCart = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("shop_cart").document(shopCart.id_order!!)
        firebaseFirestore.runTransaction { transaction: Transaction ->
            transaction.update(docShopCart,"buying",true)
            Log.d("myLogs","${firebaseAuth.currentUser?.uid} ${shopCart.id_order} ${shopCart.id_product}")
            val idOrder = "order ${UUID.randomUUID()}"
            val order = Order(
                    idOrder,
                    Date(),
                    firebaseAuth.currentUser?.uid!!,
                    shopCart.id_order,
                    shopCart.id_product
            )
            firebaseFirestore.collection("order").document(idOrder).set(order)
        }

    }

    /*override fun buyProduct(storeData: StoreData?, type: ActionType) {

        var docProd = firebaseFirestore.collection("products_2").document(storeData?.key!!)

        var docUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)


        firebaseFirestore.runTransaction { transaction: Transaction ->
            var user = transaction.get(docUser)
            var product = transaction.get(docProd)


            var money = user.getLong("money")- storeData.price?.toInt()!!
            var productCount = product.getLong("count")-1
            var userProductMas = user.data["personal_effects"] as MutableList<String>
            var userShopCart = user.data["shop_cart"] as MutableList<String>

            var list = mutableListOf<String>()
            for(i: Int in 0 until  userShopCart.size){
                if(storeData.key == userShopCart[i]){
                    continue
                }
                list.add(userShopCart[i])
            }
            transaction.update(docUser,"shop_cart",list)
            userProductMas.add(storeData.key!!)
            transaction.update(docUser,"money",money)
            transaction.update(docProd,"count",productCount)
            transaction.update(docUser,"personal_effects", userProductMas)
        }.addOnSuccessListener {
            Log.d("myLogs","successs")
        }.addOnFailureListener({
            t-> Log.d("myLogs","${t.message}")
    })
    }*/


    override fun addToBasketProduct(storeData: StoreData?, type: ActionType) {
        val docUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)
        val k = docUser.collection("shop_cart").whereEqualTo("id_user","${firebaseAuth.currentUser?.uid}").whereEqualTo("id_product","${storeData?.key}").get()
        k.addOnCompleteListener {
            k->
            if(k.result.size() == 0){
                firebaseFirestore.runTransaction { transaction ->
                    var list = mutableListOf<String>()
                    val idOrder = "s-"+UUID.randomUUID()
                    val shopCart = ShopCart(idOrder, firebaseAuth.currentUser?.uid!!,
                            storeData?.key!!,
                            Date(),
                            true,
                            false,
                            false,
                            storeData.picture!![0],
                            storeData.name+" "+storeData.products_type,
                            storeData.price+" "+storeData.currencyId
                    )
                    firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("shop_cart").document(idOrder).set(shopCart)
                }
            }

        }
    }
    /*override fun addToBasketProduct(storeData: StoreData?, type: ActionType) {
        val docUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!)

        firebaseFirestore.runTransaction { transaction: Transaction ->
            val idOrder = "s-"+UUID.randomUUID()
            val shopCart = ShopCart(idOrder, firebaseAuth.currentUser?.uid!!,
                    storeData?.key!!,
                    Date(),
                    true,
                    false,
                    false,
                    storeData.picture!![0],
                    storeData.name+" "+storeData.products_type,
                    storeData.price+" "+storeData.currencyId
                    )
            firebaseFirestore.collection("shop_cart").document(idOrder).set(shopCart)
            val user = transaction.get(docUser)

            val userShopCart = user.data["shop_cart"] as MutableList<String>

            userShopCart.add(idOrder)
            transaction.update(docUser, "shop_cart", userShopCart)
        }.addOnSuccessListener {
            Log.d("myLogs","successs")
        }.addOnFailureListener({
            t-> Log.d("myLogs","${t.message}")
        }
        )
    }*/

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

    override fun getBasketAdapter(owner: LifecycleOwner): BasketProductAdapter{
        val query = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("shop_cart")
        Log.d("myLogs","$query")
        val options = FirestoreRecyclerOptions.Builder<ShopCart>()
                .setQuery(query,ShopCart::class.java)
                .setLifecycleOwner(owner)
                .build()
        Log.d("myLogs","$options")

        return BasketProductAdapter(options)
    }


    override fun getBuyAdapter(owner: LifecycleOwner): BuyProductAdapter {
        val query = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("buying")

        val options = FirestoreRecyclerOptions.Builder<NewBuyCart>()
                .setQuery(query,NewBuyCart::class.java)
                .setLifecycleOwner(owner)
                .build()

        return BuyProductAdapter(options)
    }
}