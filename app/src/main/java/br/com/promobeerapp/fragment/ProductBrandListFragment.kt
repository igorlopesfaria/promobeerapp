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
import br.com.promobeerapp.adapter.ProductBrandListAdapter
import br.com.promobeerapp.fragment.listener.OnSelectProductBrandListListener
import br.com.promobeerapp.model.ProductBrand
import kotlinx.android.synthetic.main.fragment_product_brand_list.*
import android.content.Context


class ProductBrandListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
        OnSelectProductBrandListListener{


    private lateinit var  productBrandList: List<ProductBrand>
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    var mCallback: OnSelectProductBrandListListener? = null


    companion object {
        fun newInstance(): ProductBrandListFragment {
            return ProductBrandListFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_product_brand_list, container,
                false)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = context as OnSelectProductBrandListListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener")
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setRefreshing(true);
        delay()

    }


    private fun createBrandListAdapter(){
        brandListRecyclerView.layoutManager = LinearLayoutManager(context)
        brandListRecyclerView.adapter = ProductBrandListAdapter(productBrandList, context, this)

    }

    private fun getBrandList() {
        productBrandList =  listOf(
                ProductBrand(1, "Skol", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(2, "Brahma", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(3, "Itaipava", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(4, "Amstel", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(5, "Devassa", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(6, "Heineken", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(7, "Budweiser", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(8, "Antarctica", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(9, "Schin", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(10, "Bohemia", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(11, "Proibida", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(12, "Serramalte", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"),
                ProductBrand(13, "Corona", "https://seeklogo.com/images/B/brahma-logo-DA0DD07655-seeklogo.com.png"))
        createBrandListAdapter()
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
            getBrandList();
        }
    }

    override fun brandSelected(brand: ProductBrand) {
        mCallback?.brandSelected(brand)
    }

}


