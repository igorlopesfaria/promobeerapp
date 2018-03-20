package br.com.promobeerapp.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.ProductTypeListAdapter
import br.com.promobeerapp.model.ProductType
import kotlinx.android.synthetic.main.fragment_product_type_list.*


class ProductTypelListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    private lateinit var  productTypeList: List<ProductType>
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds


    companion object {
        fun newInstance(): ProductTypelListFragment {
            return ProductTypelListFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return  inflater.inflate(R.layout.fragment_product_type_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.setRefreshing(true);
        delay()
        swipeRefreshLayout.setOnRefreshListener(this)

    }


    private fun createProductTypeListAdapter(){
        productTypeListRecyclerView.layoutManager = LinearLayoutManager(context)
        productTypeListRecyclerView.adapter = ProductTypeListAdapter(productTypeList, context)

    }

    private fun getProductTypeList() {
        productTypeList =  listOf(
                ProductType(1,
                        "Pilsen",
                        ""),
                ProductType(2,
                        "Lager",
                        ""),
                ProductType(3,
                        "Bock",
                        ""),

                ProductType(5,
                        "Ale",
                        ""),
                ProductType(6,
                        "Weissbier",
                        ""),
                ProductType(7,
                        "India Pale Ale",
                        ""))
        createProductTypeListAdapter()
        swipeRefreshLayout.setRefreshing(false);

    }

    override fun onRefresh() {
       delay()
    }

    private fun delay() {
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    internal val mRunnable: Runnable = Runnable {
        if (!activity.isFinishing) {
            getProductTypeList();
        }
    }

}
