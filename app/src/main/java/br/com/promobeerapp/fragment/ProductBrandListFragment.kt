package br.com.promobeerapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.ProductBrandListAdapter
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductType
import kotlinx.android.synthetic.main.fragment_product_brand_list.*
import java.io.IOException


class ProductBrandListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemSelectedListener<ProductBrand> {

    private val productBrandList: MutableList<ProductBrand> = mutableListOf()
    private var isFilter: Boolean = false

    companion object {
        private val ARG_IS_FILTER: String = "ARG_IS_FILTER"
        fun newInstance(isFilter: Boolean): ProductBrandListFragment {
            val args = Bundle()
            args.putBoolean(ProductBrandListFragment.ARG_IS_FILTER, isFilter)
            val fragment = ProductBrandListFragment()
            fragment.arguments = args
            return fragment

        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        isFilter= arguments?.getBoolean(ProductBrandListFragment.ARG_IS_FILTER)!!

        return inflater.inflate(R.layout.fragment_product_brand_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.setOnRefreshListener(this)

        swipeRefreshLayout.isRefreshing = true
        prepareLoadingLayout()
        getBrandList()


        if(isFilter)
            titleTXV.visibility = View.GONE
        else
            titleTXV.visibility = View.VISIBLE

        tryAgainBTN.setOnClickListener {
            swipeRefreshLayout.isRefreshing = true;
            onRefresh()
        }

    }


    private fun createBrandListAdapter() {
        prepareRecyclerviewLayout()
        brandListRecyclerView?.layoutManager = GridLayoutManager(context, 2)
        brandListRecyclerView?.adapter = ProductBrandListAdapter(productBrandList, context, this)

    }

    private fun getBrandList() {

        ProductWebClient().listBrands(object : CallbackServiceResponse<List<ProductBrand>> {
            override fun success(response: List<ProductBrand>) {
                prepareSucessScreen(response)
            }

            override fun fail(throwable: Throwable) {
                prepareErrorScreen(throwable)
            }
        })


    }
    fun prepareSucessScreen(response: List<ProductBrand>){
        this@ProductBrandListFragment.productBrandList.clear()
        this@ProductBrandListFragment.productBrandList.addAll(response)
        createBrandListAdapter()
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

        if (this@ProductBrandListFragment.productBrandList.size == 0) {
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
        feedbackTitleTXV?.text = context?.getText(R.string.loading_product_brand_list)
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
        if (this@ProductBrandListFragment.productBrandList.size == 0)
            prepareLoadingLayout()

        getBrandList()
    }

    override fun onItemSelected(productBrand: ProductBrand) {
        if(!isFilter) {
            (activity as MainActivity).changeFragment(ProductTypeListFragment.newInstance(productBrand, false), true)
        }else {
            (activity as MainActivity).preffs?.setBrandFilter(productBrand)
            (activity as MainActivity).onBackPressed()
        }
    }

}


