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
import br.com.promobeerapp.adapter.ProductSizeListAdapter
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductSize
import br.com.promobeerapp.model.ProductType
import kotlinx.android.synthetic.main.fragment_product_size_list.*
import java.io.IOException


class ProductSizeListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemSelectedListener<ProductSize> {

    private val productSizeList: MutableList<ProductSize> = mutableListOf()

    private val type1SizeList: MutableList<ProductSize> = mutableListOf()
    private val type2SizeList: MutableList<ProductSize> = mutableListOf()


    private var productBrand: ProductBrand? = null
    private var productType: ProductType? = null
    private var isFilter: Boolean = false

    companion object {
        private val ARG_PRODUCT_BRAND: String = "ARG_PRODUCT_BRAND"
        private val ARG_PRODUCT_TYPE: String = "ARG_PRODUCT_TYPE"
        private val ARG_IS_FILTER: String = "ARG_IS_FILTER"
        var productSizeSelected: ProductSize? = null

        fun newInstance(productBrand: ProductBrand?, productType: ProductType?, isFilter:Boolean): ProductSizeListFragment {
            val args = Bundle()
            args.putSerializable(ARG_PRODUCT_BRAND, productBrand)
            args.putSerializable(ARG_PRODUCT_TYPE, productType)
            args.putBoolean(ARG_IS_FILTER, isFilter)
            val fragment = ProductSizeListFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        productBrand= arguments?.getSerializable(ARG_PRODUCT_BRAND) as ProductBrand?
        productType= arguments?.getSerializable(ARG_PRODUCT_TYPE) as ProductType?
        isFilter= arguments?.getBoolean(ARG_IS_FILTER)!!

        return  inflater.inflate(R.layout.fragment_product_size_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout?.setRefreshing(true);
        swipeRefreshLayout?.setOnRefreshListener(this)
        swipeRefreshLayout.isRefreshing = true
        prepareLoadingLayout()
        getSizeList()
        productSizeSelected =null
        tryAgainBTN.setOnClickListener {
            swipeRefreshLayout?.isRefreshing = true;
            onRefresh()
        }

        size1IMG.setOnClickListener{
            filterSizeByMaterial()
            prepareRecyclerviewLayout();
            sizeTitleTXV.text = getString(R.string.choice_product_size_bottle)

            if(type1SizeList.size>0)
                createProductSizeListAdapter(type1SizeList)
            else
                prepareFeedbackSizeLayout(getString(R.string.no_product_with_this_size))

        }

        size2IMG.setOnClickListener{
            filterSizeByMaterial()
            prepareRecyclerviewLayout();
            sizeTitleTXV.text = getString(R.string.choice_product_size_can)

            if(type2SizeList.size>0)
                createProductSizeListAdapter(type2SizeList)
            else
                prepareFeedbackSizeLayout(getString(R.string.no_product_with_this_size))

        }


    }


    private fun filterSizeByMaterial() {
        var i = 0
        type1SizeList.clear()
        type2SizeList.clear()
        while (i < productSizeList.size) {
            if(productSizeList[i].material?.equals("Lata", true)){
                type2SizeList.add(productSizeList[i])
            }else{
                type1SizeList.add(productSizeList[i])
            }
            i++
        }
    }


    private fun createProductSizeListAdapter(typeCorrectSizeList: MutableList<ProductSize>) {
        productSizeListRecyclerView?.layoutManager = LinearLayoutManager(context)
        productSizeListRecyclerView?.adapter = ProductSizeListAdapter(typeCorrectSizeList, context,  this)

    }

    private fun getSizeList() {


       if(productBrand !=null && productType !=null ) {
           ProductWebClient().listSizeByFilter(productBrand!!, productType!!, object : CallbackServiceResponse<List<ProductSize>> {
               override fun success(response: List<ProductSize>) {
                   prepareSucessScreen(response)
               }

               override fun fail(throwable: Throwable) {
                   prepareErrorScreen(throwable)
               }
           })
       }else{
               ProductWebClient().listSize( object : CallbackServiceResponse<List<ProductSize>> {
                   override fun success(response: List<ProductSize>) {
                       prepareSucessScreen(response)
                   }

                   override fun fail(throwable: Throwable) {
                       prepareErrorScreen(throwable)
                   }
               })

           }





    }

    fun prepareSucessScreen(response: List<ProductSize>) {
        this@ProductSizeListFragment.productSizeList.clear()
        this@ProductSizeListFragment.productSizeList.addAll(response)
        prepareFilterLayout()
        swipeRefreshLayout?.isRefreshing = false
    }

    fun prepareErrorScreen(throwable: Throwable) {
        swipeRefreshLayout.isRefreshing = false
        var title:String = getString(R.string.error)
        var subtitle:String = getString(R.string.problem_server_connection)
        var drawable: Drawable? = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_error) }

        if (throwable is IOException) {
            title = getString(R.string.check_your_conection)
            subtitle =  getString(R.string.try_again_when_online)
            drawable  = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_offline) }
        }

        if (this@ProductSizeListFragment.productSizeList.size == 0) {
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
        feedbackTitleTXV?.text = context?.getText(R.string.loading_product_size_list)
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

    private fun prepareFilterLayout() {
        feedbackTitleTXV?.visibility = View.GONE
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        mainLayout?.visibility = View.VISIBLE
        listView?.visibility = View.GONE
        feedbackSizeTitleTXV?.visibility = View.GONE

    }

    private fun prepareRecyclerviewLayout() {
        feedbackTitleTXV?.visibility = View.GONE
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        mainLayout?.visibility = View.VISIBLE
        listView?.visibility = View.VISIBLE
        feedbackSizeTitleTXV?.visibility = View.GONE
    }

    private fun prepareFeedbackSizeLayout(msg: String) {
        feedbackTitleTXV?.visibility = View.GONE
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        mainLayout?.visibility = View.VISIBLE
        listView?.visibility = View.GONE
        feedbackSizeTitleTXV?.visibility = View.VISIBLE
        feedbackSizeTitleTXV.text = msg

    }

    override fun onRefresh() {
        if (this@ProductSizeListFragment.productSizeList.size == 0)
            prepareLoadingLayout()

        getSizeList()
    }


    override fun onItemSelected(productSize: ProductSize) {

        if(!isFilter){
            (activity as BaseActivity).changeFragment(PromoRegisterFragment.newInstance(productBrand, productType ,productSize), true)
        }else {
            productSizeSelected = productSize
            (activity as BaseActivity).onBackPressed()
        }

    }

}