package br.com.promobeerapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.ProductTypeListAdapter
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductType
import kotlinx.android.synthetic.main.fragment_product_type_list.*
import java.io.IOException


class ProductTypeListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemSelectedListener<ProductType> {

    private val productTypeList: MutableList<ProductType> = mutableListOf()


    private var productBrand:ProductBrand? = null


    companion object {
        private val ARG_PRODUCT_BRAND: String = "ARG_PRODUCT_BRAND"

        fun newInstance(productBrand: ProductBrand?): ProductTypeListFragment {
            val args = Bundle()
            args.putSerializable(ARG_PRODUCT_BRAND, productBrand)
            val fragment = ProductTypeListFragment()
            fragment.arguments = args
            return fragment
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
        swipeRefreshLayout.setOnRefreshListener(this)
        productBrand= arguments?.getSerializable(ARG_PRODUCT_BRAND) as ProductBrand?

        swipeRefreshLayout.isRefreshing = true
        prepareLoadingLayout()
        getTypeList()

        tryAgainBTN.setOnClickListener {
            swipeRefreshLayout.isRefreshing = true;
            onRefresh()
        }


    }


    private fun createProductTypeListAdapter(){
        productTypeListRecyclerView.layoutManager = LinearLayoutManager(context)
        productTypeListRecyclerView.adapter = ProductTypeListAdapter(productTypeList, context, this)

    }

    private fun getTypeList() {

        productBrand?.let {
            ProductWebClient().listTypeByFilter(it, object : CallbackServiceResponse<List<ProductType>> {
                override fun success(productTypeList: List<ProductType>) {
                    this@ProductTypeListFragment.productTypeList.clear()
                    this@ProductTypeListFragment.productTypeList.addAll(productTypeList)
                    prepareRecyclerviewLayout()
                    createProductTypeListAdapter()
                    swipeRefreshLayout.isRefreshing = false
                }
    
                override fun fail(throwable: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                    var title:String = getString(R.string.error)
                    var subtitle:String = getString(R.string.problem_server_connection)
                    var drawable:Drawable? = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_error) }
    
                    if (throwable is IOException) {
                        title = getString(R.string.check_your_conection)
                        subtitle =  getString(R.string.try_again_when_online)
                        drawable  = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_offline) }
                    }
    
                    if (this@ProductTypeListFragment.productTypeList.size == 0) {
                        prepareFeedbackLayout(title,
                                subtitle,
                                drawable)
                    }else{
                        view?.let { Snackbar.make(it, subtitle, Snackbar.LENGTH_LONG).show() }
                    }
    
                }
            })
        }


    }
    private fun prepareLoadingLayout() {
        feedbackLayout.visibility = View.VISIBLE
        feedbackTitleTXV.visibility = View.VISIBLE
        feedbackTitleTXV.text = context?.getText(R.string.loading_product_type_list)
        feedbackIMG.visibility = View.GONE
        feedbackSubtitleTXV.visibility = View.GONE
        tryAgainBTN.visibility = View.GONE
        mainLayout.visibility = View.GONE
    }

    private fun prepareFeedbackLayout(title: String, subtitle: String, drawable: Drawable?) {
        feedbackLayout.visibility = View.VISIBLE
        feedbackTitleTXV.visibility = View.VISIBLE
        feedbackTitleTXV.text = title
        feedbackSubtitleTXV.visibility = View.VISIBLE
        feedbackSubtitleTXV.text = subtitle

        drawable?.let {
            feedbackIMG.visibility = View.VISIBLE
            feedbackIMG.setImageDrawable(drawable)
        }
        tryAgainBTN.visibility = View.VISIBLE
        mainLayout.visibility = View.GONE
    }

    private fun prepareRecyclerviewLayout() {
        feedbackTitleTXV.visibility = View.GONE
        feedbackIMG.visibility = View.GONE
        feedbackSubtitleTXV.visibility = View.GONE
        tryAgainBTN.visibility = View.GONE
        mainLayout.visibility = View.VISIBLE
    }
    override fun onRefresh() {
        if (this@ProductTypeListFragment.productTypeList.size == 0)
            prepareLoadingLayout()

        getTypeList()
    }


    override fun onItemSelected(productType: ProductType) {
        (activity as MainActivity).changeFragment(ProductSizeListFragment.newInstance(productBrand,productType), true)
    }

}
