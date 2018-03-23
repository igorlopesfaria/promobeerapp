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
import br.com.promobeerapp.adapter.ProductSizeListAdapter
import br.com.promobeerapp.model.ProductSize
import kotlinx.android.synthetic.main.fragment_product_size_list.*


class ProductSizeListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    private lateinit var  productSizeList: List<ProductSize>
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds


    companion object {
        fun newInstance(): ProductSizeListFragment {
            return ProductSizeListFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{

        return  inflater.inflate(R.layout.fragment_product_size_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.setRefreshing(true);
        delay()
        swipeRefreshLayout.setOnRefreshListener(this)

    }


    private fun createProductSizeListAdapter(){
        productSizeListRecyclerView.layoutManager = LinearLayoutManager(context)
        productSizeListRecyclerView.adapter = ProductSizeListAdapter(productSizeList, context)

    }

    private fun getProductSizeList() {
        productSizeList =  listOf(
                ProductSize(1,
                        "269ml",
                        "Lata",
                        ""),
                ProductSize(2,
                        "350ml",
                        "Lata",
                        ""),
                ProductSize(2,
                        "356ml",
                        "Longneck",
                        ""),
                ProductSize(3,
                        "473ml",
                        "Longneck",
                        ""),

                ProductSize(5,
                        "600ml",
                        "Garrafa",
                        ""),
                ProductSize(6,
                        "1L",
                        "Garrafa",
                        ""))
        createProductSizeListAdapter()
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
        if ((activity?.isFinishing)==false) {
            getProductSizeList();
        }
    }

}
