package com.oleg.myfashionclient.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.oleg.myfashionclient.R
import com.oleg.myfashionclient.ui.BaseFragment
import com.oleg.myfashionclient.viewmodels.IAllProducts
import com.oleg.myfashionclient.viewmodels.MainViewModel
import com.oleg.myfashionclient.viewmodels.MainViewModelFactory
import javax.inject.Inject


class SearchFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: MainViewModelFactory

    val vm: IAllProducts by lazy {
        ViewModelProviders.of(this,vmFactory)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View  =  inflater.inflate(R.layout.fragment_search, container, false)
        initUI(view)
        return view
    }

    private fun initUI(v: View) {
        setRecycler(v.findViewById(R.id.recycler_search))
    }

    private fun setRecycler(recyc: RecyclerView) {
        recyc.layoutManager = LinearLayoutManager(context!!)
        //val adapter
    }

    companion object {
        fun newInstance(): SearchFragment{
            return SearchFragment()
        }
    }
    private fun displayMessage(message: String){
        Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
    }

}
