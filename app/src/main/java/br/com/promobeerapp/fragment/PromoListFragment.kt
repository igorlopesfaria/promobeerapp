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
import br.com.promobeerapp.adapter.PromoListAdapter
import br.com.promobeerapp.connection.PromoWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
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
                    (activity as MainActivity).changeFragment(ProductBrandListFragment.newInstance(), true)
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

        getPromoList()

    }

    private fun prepareLoadingLayout() {
        feedbackLayout?.visibility = View.VISIBLE
        feedbackTitleTXV?.visibility = View.VISIBLE
        feedbackTitleTXV?.text = context?.getText(R.string.loading_product_type_list)
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
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
    }

    private fun prepareRecyclerviewLayout() {
        feedbackTitleTXV?.visibility = View.GONE
        feedbackIMG?.visibility = View.GONE
        feedbackSubtitleTXV?.visibility = View.GONE
        tryAgainBTN?.visibility = View.GONE
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
        paginate = Paginate.with(promoListRecyclerView, callbacks)
                .addLoadingListItem(true)
//                .setLoadingListItemCreator(CustomLoadingListItemCreator())
                .build();
    }

    val callbacks = object : Paginate.Callbacks {
        override fun onLoadMore() {
            // Load next page of data (e.g. network or database)
            Log.d("Paginate", "onLoadMore");
            loading = true
//            delayPagination()
        }

        override fun isLoading(): Boolean {
            // Indicate whether new page loading is in progress or not
            Log.d("Paginate", "isLoading");
            return loading
        }

        override fun hasLoadedAllItems(): Boolean {
            // Indicate whether all data (pages) are loaded or not
            Log.d("Paginate", "hasLoadedAllItems" );
            return hasLoadedAllItems
        }
    }


    private fun populateMoreOneItem() {
//        var promoAuxList= listOf(
//                Promo(1,
//                        Product(1,
//                                ProductBrand(1, "Cerveja Eisenbahn", ""),
//                                ProductSize(3, "350ml", "Lata", ""),
//                                ProductType(2, "Pilsen Puro Malte", ""),
//                                "https://savegnago.vteximg.com.br/arquivos/ids/285576-240-240/CERVEJA-EISENBAHN-350ML-LT-PILSEN.jpg"),
//                        Address("-12.990706",
//                                "-38.473735",
//                                "Extra - Brotas, Salvador - Bahia, Brasil",
//                                "Brotas"),
//                        "",
//                        "R$ 1,90",
//                        "18/03/18",
//                        "16/03/18 - 10:00"),
//                Promo(1,
//                        Product(1,
//                                ProductBrand(1, "Cerveja Eisenbahn", ""),
//                                ProductSize(3, "350ml", "Lata", ""),
//                                ProductType(2, "Pilsen Puro Malte", ""),
//                                "https://savegnago.vteximg.com.br/arquivos/ids/285576-240-240/CERVEJA-EISENBAHN-350ML-LT-PILSEN.jpg"),
//                        Address("-12.990706",
//                                "-38.473735",
//                                "Extra - Brotas, Salvador - Bahia, Brasil",
//                                "Brotas"),
//                        "",
//                        "R$ 1,90",
//                        "18/03/18",
//                        "16/03/18 - 10:00"))
//        promoList.addAll(promoAuxList)
//        promoListAdapter?.notifyDataSetChanged()
//
//        loading = false
//        hasLoadedAllItems= true
//        paginate?.unbind()
    }

    fun scrollMyListViewToBottom() {
        promoListRecyclerView.post(Runnable {
            // Select the last row so it will scroll into view...
            promoListRecyclerView.smoothScrollToPosition( (promoListAdapter!!.getCount()) - 1)
        })
    }
    private fun getPromoList() {
        PromoWebClient().list(object : CallbackServiceResponse<List<Promo>> {
            override fun success(response: List<Promo>) {
                    this@PromoListFragment.promoList.clear()
                    this@PromoListFragment.promoList.addAll(promoList)
                    prepareRecyclerviewLayout()
                    createPromoListAdapter()
                    swipeRefreshLayout?.isRefreshing = false
                }

                override fun fail(throwable: Throwable) {
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
        })
    }

}