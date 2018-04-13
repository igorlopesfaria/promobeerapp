package br.com.promobeerapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.R.string.no_promo_found
import br.com.promobeerapp.adapter.PromoListAdapter
import br.com.promobeerapp.connection.PromoWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.Promo
import com.paginate.Paginate
import kotlinx.android.synthetic.main.fragment_promo_list.*
import java.io.IOException


class PromoListFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener, OnItemSelectedListener<Promo>{


    var promoList: MutableList<Promo> = mutableListOf()
    var mDelayHandler: Handler? = null
    val SPLASH_DELAY: Long = 3000 //3 seconds
    var hasLoadedAllItems:Boolean = false
    var GRID_SPAN:Int = 3
    var loading: Boolean = false
    var paginate: Paginate? = null

    var promoListAdapter:PromoListAdapter? = null

    companion object {
        fun newInstance(): PromoListFragment {
            return PromoListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_promo_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.setOnRefreshListener(this)
        floatingActionButton.setOnClickListener {

            if((activity as MainActivity).verifyPermission())
                if (activity is MainActivity )
                    (activity as MainActivity).changeFragment(ProductBrandListFragment.newInstance(false), true)
        }

        tryAgainBTN.setOnClickListener{
            swipeRefreshLayout.isRefreshing = true;
            onRefresh()
        }
        prepareFeedbackLayout(activity?.getText(R.string.check_your_conection).toString(),
                activity?.getText(R.string.try_again_when_online).toString(),
                context?.let {
                    ContextCompat.getDrawable(it, R.drawable.ic_offline)
                })
        prepareLoadingLayout()
        getPromoList()

    }

    private fun prepareLoadingLayout() {
        feedbackLayout?.visibility = View.VISIBLE
        feedbackTitleTXV?.visibility = View.VISIBLE
        feedbackTitleTXV?.text = context?.getText(R.string.loading_promo_list)
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        promoListRecyclerView?.visibility = View.GONE
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
        promoListRecyclerView?.visibility = View.GONE
    }

    private fun prepareRecyclerviewLayout() {
        feedbackTitleTXV?.visibility = View.GONE
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
        promoListRecyclerView?.visibility = View.VISIBLE
    }
    override fun onRefresh() {
        if (this@PromoListFragment.promoList.size == 0)
            prepareLoadingLayout()

        getPromoList()
    }


    override fun onItemSelected(promo: Promo) {
//        (activity as MainActivity).changeFragment(ProductSizeListFragment.newInstance(productBrand,productType), true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_promo_list, menu)
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.item_menu_notification ->{
                Toast.makeText(activity, "Clicou not", Toast.LENGTH_SHORT)
                return true

            }
            R.id.item_menu_filter ->{
                if (activity is MainActivity )
                    (activity as MainActivity).changeFragment(PromoFilterFragment.newInstance(), true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun createPromoListAdapter() {
        promoListRecyclerView?.layoutManager = LinearLayoutManager(context)
        promoListAdapter= PromoListAdapter(promoList, context)
        promoListRecyclerView?.adapter = promoListAdapter
    }


    private fun getPromoList() {
        val productBrandFiltered = (activity as MainActivity).preffs?.getBrandFilter()
        val productTypeFiltered = (activity as MainActivity).preffs?.getTypeFilter()
        val productSizeFiltered = (activity as MainActivity).preffs?.getSizeFilter()

        if(productBrandFiltered==null && productTypeFiltered==null && productSizeFiltered==null) {
            PromoWebClient().list(object : CallbackServiceResponse<List<Promo>> {
                override fun success(response: List<Promo>) {
                    prepareSucessScreen(response)
                }

                override fun fail(throwable: Throwable) {
                    prepareErrorScreen(throwable)
                }
            })
        }else{
            PromoWebClient().listByFilter(productBrandFiltered, productTypeFiltered, productSizeFiltered ,object : CallbackServiceResponse<List<Promo>> {
                override fun success(response: List<Promo>) {
                    if(response.size>0)
                        prepareSucessScreen(response)
                    else {
                        val drawable = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_sad) }

                        prepareFeedbackLayout(getString(R.string.no_promo_found),
                                getString(R.string.try_again_later),
                                drawable)
                        swipeRefreshLayout?.isRefreshing = false

                    }
                }

                override fun fail(throwable: Throwable) {
                    prepareErrorScreen(throwable)
                }
            })

        }
    }

    fun prepareSucessScreen(response: List<Promo>){
        this@PromoListFragment.promoList.clear()
        this@PromoListFragment.promoList.addAll(response)
        prepareRecyclerviewLayout()
        createPromoListAdapter()
        swipeRefreshLayout?.isRefreshing = false

    }

    fun prepareErrorScreen(throwable: Throwable){
        swipeRefreshLayout.isRefreshing = false
        var title: String = getString(R.string.error)
        var subtitle: String = getString(R.string.problem_server_connection)
        var drawable: Drawable? = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_error) }

        if (throwable is IOException) {
            title = getString(R.string.check_your_conection)
            subtitle = getString(R.string.try_again_when_online)
            drawable = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_offline) }
        }

        if (this@PromoListFragment.promoList.size == 0) {
            prepareFeedbackLayout(title,
                    subtitle,
                    drawable)
        } else {
            view?.let { Snackbar.make(it, subtitle, Snackbar.LENGTH_LONG).show() }
        }
    }
}