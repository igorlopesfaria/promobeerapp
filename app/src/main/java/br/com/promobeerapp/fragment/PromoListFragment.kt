package br.com.promobeerapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.CustomLoadingListItemCreator
import br.com.promobeerapp.adapter.PromoListAdapter
import br.com.promobeerapp.model.*
import com.paginate.Paginate
import kotlinx.android.synthetic.main.fragment_promo_list.*


class PromoListFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener{


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
        floatingActionButton.setOnClickListener { view ->
            if (activity is MainActivity )
                (activity as MainActivity).changeFragment(PromoRegisterFragment.newInstance(), true)
        }

        tryAgainBTN.setOnClickListener{view->
            swipeRefreshLayout.isRefreshing = true;
            onRefresh()
        }
        prepareFeedbackLayout(activity?.getText(R.string.check_your_conection).toString(),
                activity?.getText(R.string.try_again_when_online).toString(),
                context?.let {
                    ContextCompat.getDrawable(it, R.drawable.ic_offline)
                })

    }

    private fun prepareLoadingLayout() {
        feedbackTitleTXV.visibility = View.VISIBLE
        feedbackTitleTXV.text = context?.getText(R.string.loading_promo_list)
        feedbackIMG.visibility = View.GONE
        feedbackSubtitleTXV.visibility = View.GONE
        tryAgainBTN.visibility = View.GONE
        promoListRecyclerView.visibility = View.GONE
    }
    private fun prepareFeedbackLayout(title: String, subtitle: String, drawable: Drawable?) {

        feedbackTitleTXV.visibility = View.VISIBLE
        feedbackTitleTXV.text = title
        feedbackSubtitleTXV.visibility = View.VISIBLE
        feedbackSubtitleTXV.text = subtitle

        drawable?.let {
            feedbackIMG.visibility = View.VISIBLE
            feedbackIMG.setImageDrawable(drawable)
        }
        tryAgainBTN.visibility = View.VISIBLE
        promoListRecyclerView.visibility = View.GONE
    }
    private fun prepareRecyclerviewLayout() {
        feedbackTitleTXV.visibility = View.GONE
        feedbackIMG.visibility = View.GONE
        feedbackSubtitleTXV.visibility = View.GONE
        tryAgainBTN.visibility = View.GONE
        promoListRecyclerView.visibility = View.VISIBLE
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
            delayPagination()
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
        var promoAuxList= listOf(
                Promo(1,
                        Product(1,
                                ProductBrand(1, "Cerveja Eisenbahn", ""),
                                ProductSize(3, "350ml", "Lata", ""),
                                ProductType(2, "Pilsen Puro Malte", ""),
                                "https://savegnago.vteximg.com.br/arquivos/ids/285576-240-240/CERVEJA-EISENBAHN-350ML-LT-PILSEN.jpg"),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(1,
                        Product(1,
                                ProductBrand(1, "Cerveja Eisenbahn", ""),
                                ProductSize(3, "350ml", "Lata", ""),
                                ProductType(2, "Pilsen Puro Malte", ""),
                                "https://savegnago.vteximg.com.br/arquivos/ids/285576-240-240/CERVEJA-EISENBAHN-350ML-LT-PILSEN.jpg"),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"))
        promoList.addAll(promoAuxList)
        promoListAdapter?.notifyDataSetChanged()

        loading = false
        hasLoadedAllItems= true
        paginate?.unbind()
    }

    fun scrollMyListViewToBottom() {
        promoListRecyclerView.post(Runnable {
            // Select the last row so it will scroll into view...
            promoListRecyclerView.smoothScrollToPosition( (promoListAdapter!!.getCount()) - 1)
        })
    }
    private fun getPromoList() {
        var aas = listOf(
                Promo(1,
                        Product(1,
                                ProductBrand(1, "Cerveja Eisenbahn", ""),
                                ProductSize(3, "350ml", "Lata", ""),
                                ProductType(2, "Pilsen Puro Malte", ""),
                                "https://savegnago.vteximg.com.br/arquivos/ids/285576-240-240/CERVEJA-EISENBAHN-350ML-LT-PILSEN.jpg"),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(2,
                        Product(1,
                                ProductBrand(1, "Cerveja Skol", ""),
                                ProductSize(3, "269ml", "Lata", ""),
                                ProductType(2, "Pilsen", ""),
                                "https://static.carrefour.com.br/medias/sys_master/images/images/hf6/h63/h00/h00/9218294972446.jpg"),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(3,
                        Product(1,
                                ProductBrand(1, "Cerveja Heineken", ""),
                                ProductSize(3, "330ml", "Long Neck", ""),
                                ProductType(2, "Pilsen", ""),
                                "https://savegnago.vteximg.com.br/arquivos/ids/273281-240-240/figura-1frente.jpg"),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(4,
                        Product(1,
                                ProductBrand(11, "Cerveja Proibida", ""),
                                ProductSize(2, "330ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://savegnago.vteximg.com.br/arquivos/ids/281640-240-240/CERVEJA-PROIBIDA-330ML-PURO-MALTE-1.jpg"),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"),
                Promo(5,
                        Product(1,
                                ProductBrand(4, "Cerveja Amstel", ""),
                                ProductSize(3, "473ml", "Longneck", ""),
                                ProductType(2, "Lager", ""),
                                "https://superprix.vteximg.com.br/arquivos/ids/168453-600-600/Cerveja-Amstel-Lager-600ml.jpg\""),
                        Address("-12.990706",
                                "-38.473735",
                                "Extra - Brotas, Salvador - Bahia, Brasil",
                                "Brotas"),
                        "",
                        "R$ 1,90",
                        "18/03/18",
                        "16/03/18 - 10:00"))

        promoList.addAll(aas)
        if(promoList?.size>0){
            prepareRecyclerviewLayout()
            createPromoListAdapter()
        }else{
            prepareFeedbackLayout(activity?.getText(R.string.no_promo_found).toString(),
                    activity?.getText(R.string.try_again_later).toString(),
                    null)
        }
        swipeRefreshLayout?.setRefreshing(false);
    }

    override fun onRefresh() {
        delay()
    }

    private fun delay() {
        if(promoList?.size==0)
            prepareLoadingLayout()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    internal val mRunnable: Runnable = Runnable {
        if (activity?.isFinishing == false) {
            getPromoList();
        }
    }

    private fun delayPagination() {
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnableItem, SPLASH_DELAY)
    }

    internal val mRunnableItem: Runnable = Runnable {
        if (activity?.isFinishing == false) {
            populateMoreOneItem();
        }
    }

}