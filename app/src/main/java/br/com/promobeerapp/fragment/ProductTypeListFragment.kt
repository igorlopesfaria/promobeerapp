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
import br.com.promobeerapp.BaseActivity
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
    private var isFilter: Boolean  = false


    companion object {
        private val ARG_PRODUCT_BRAND: String = "ARG_PRODUCT_BRAND"
        private val ARG_IS_FILTER: String = "ARG_IS_FILTER"
        var productTypeSelected: ProductType? = null

        fun newInstance(productBrand: ProductBrand?,isFilter: Boolean): ProductTypeListFragment {
            val args = Bundle()
            args.putSerializable(ARG_PRODUCT_BRAND, productBrand)
            args.putBoolean(ARG_IS_FILTER, isFilter)
            val fragment = ProductTypeListFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        productBrand= arguments?.getSerializable(ARG_PRODUCT_BRAND) as ProductBrand?
        isFilter= arguments?.getBoolean(ARG_IS_FILTER)!!

        return  inflater.inflate(R.layout.fragment_product_type_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout?.setRefreshing(true);
        swipeRefreshLayout?.setOnRefreshListener(this)

        swipeRefreshLayout?.isRefreshing = true
        prepareLoadingLayout()
        getTypeList()
        productTypeSelected = null

        tryAgainBTN.setOnClickListener {
            swipeRefreshLayout.isRefreshing = true;
            onRefresh()
        }

        if(isFilter) {
            titleTXV.visibility = View.GONE
            bottlesLayout.visibility = View.GONE
            lineIMG.visibility = View.GONE
        }else {
            titleTXV.visibility = View.VISIBLE
            bottlesLayout.visibility = View.VISIBLE
            lineIMG.visibility = View.VISIBLE
        }

    }


    private fun createProductTypeListAdapter(){
        productTypeListRecyclerView?.layoutManager = LinearLayoutManager(context)
        productTypeListRecyclerView?.adapter = ProductTypeListAdapter(productTypeList, context, this)

    }

    private fun getTypeList() {

        if(productBrand!=null )
            ProductWebClient().listTypeByFilter(productBrand!!, object : CallbackServiceResponse<List<ProductType>> {
                override fun success(productTypeList: List<ProductType>) {
                    prepareSucessScreen(productTypeList)
                }
    
                override fun fail(throwable: Throwable) {
                    prepareErrorScreen(throwable)

                }
            })
        else
            ProductWebClient().listType( object : CallbackServiceResponse<List<ProductType>> {
                override fun success(response: List<ProductType>) {
                    prepareSucessScreen(response)
                }

                override fun fail(throwable: Throwable) {
                    prepareErrorScreen(throwable)

                }
            })



    }

    fun prepareSucessScreen(response: List<ProductType>){
        this@ProductTypeListFragment.productTypeList.clear()
        this@ProductTypeListFragment.productTypeList.addAll(response)
        prepareRecyclerviewLayout()
        createProductTypeListAdapter()
        swipeRefreshLayout?.isRefreshing = false

    }

    fun prepareErrorScreen(throwable: Throwable){
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
    private fun prepareLoadingLayout() {
        feedbackLayout?.visibility = View.VISIBLE
        feedbackTitleTXV?.visibility = View.VISIBLE
        feedbackTitleTXV?.text = context?.getText(R.string.loading_product_type_list)
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        mainLayout?.visibility = View.GONE
    }

    private fun prepareFeedbackLayout(title: String, subtitle: String, drawable: Drawable?) {
        feedbackLayout?.visibility = View.VISIBLE
        feedbackTitleTXV?.visibility = View.VISIBLE
        feedbackTitleTXV?.text = title
        feedbackSubtitleTXV?.visibility = View.VISIBLE
        feedbackSubtitleTXV?.text = subtitle

        drawable?.let {
            feedbackIMG?.visibility = View.VISIBLE
            feedbackIMG?.setImageDrawable(drawable)
        }
        tryAgainBTN?.visibility = View.VISIBLE
        mainLayout?.visibility = View.GONE
    }

    private fun prepareRecyclerviewLayout() {
        feedbackTitleTXV?.visibility = View.GONE
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        mainLayout?.visibility = View.VISIBLE
    }
    override fun onRefresh() {
        if (this@ProductTypeListFragment.productTypeList.size == 0)
            prepareLoadingLayout()

        getTypeList()
    }


    override fun onItemSelected(productType: ProductType) {
        if(!isFilter){
            (activity as BaseActivity).changeFragment(ProductSizeListFragment.newInstance(productBrand,productType, false), true)
        }else {
            productTypeSelected = productType
            (activity as BaseActivity).onBackPressed()
        }
    }

}
