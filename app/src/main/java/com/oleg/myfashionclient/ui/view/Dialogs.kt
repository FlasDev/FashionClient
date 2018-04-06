package com.oleg.myfashionclient.ui.view

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.model.StoreData
import io.reactivex.Observable


fun showBalanceDialog(context: Context): Observable<String>{
    return Observable.create({
        subscriber ->
        var dialog = MaterialDialog.Builder(context)
                .title("Пополнение баланса")
                .content(R.string.title_balance)
                .items(R.array.balance_type)
                .itemsCallbackSingleChoice(-1) { dialog, itemView, which, text ->
                    subscriber.onNext(text.toString())
                    return@itemsCallbackSingleChoice true
                }
                .positiveText("Пополнить")
                .negativeText("Отмена")
                .show()
    })
}

fun showBuyDialog(context: Context, storeData: StoreData): Observable<String>{
    return Observable.create({
        subscriber->
        var dialog = MaterialDialog.Builder(context)
                .title("Покупка")
                .content("Вы уверены, что хотите купить ${storeData.name} ${storeData.products_type} за ${storeData.price} ${storeData.currencyId}")
                .positiveText("Купить")
                .onPositive({dialog, which -> subscriber.onNext(which.name)})
                .negativeText("Отмена")
                .show()
    })
}