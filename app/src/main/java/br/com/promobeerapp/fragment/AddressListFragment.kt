package br.com.promobeerapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.AddressListAdapter
import br.com.promobeerapp.connection.GoogleWebClient
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.*
import kotlinx.android.synthetic.main.fragment_address_list.*
import java.io.IOException


class AddressListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnItemSelectedListener<GoogleAddress> {

    private val googleAddressList: MutableList<GoogleAddress> = mutableListOf()

    companion object {
        fun newInstance(): AddressListFragment {
            return AddressListFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_address_list, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout.setOnRefreshListener(this)

        swipeRefreshLayout.isRefreshing = true
        prepareLoadingLayout()
        getAddressList()


        tryAgainBTN.setOnClickListener {
            swipeRefreshLayout.isRefreshing = true;
            onRefresh()
        }

    }


    private fun createAddressListAdapter() {
        prepareRecyclerviewLayout()
        addressListRecyclerView?.layoutManager = LinearLayoutManager(context)
        addressListRecyclerView?.adapter = AddressListAdapter(googleAddressList, context, this)

    }


    private fun getAddressList() {
        val location = (activity as MainActivity)?.mLastLocation?.latitude?.toString()+","+(activity as MainActivity)?.mLastLocation?.longitude
        GoogleWebClient().searchByDistance(location, getString(R.string.rank_by_distance),
                getString(R.string.type_supermarket),getString(R.string.google_maps_id),
                object : CallbackServiceResponse<List<GoogleAddress>> {

                override fun success(googleAddressList: List<GoogleAddress>) {
                    this@AddressListFragment.googleAddressList.clear()
                    this@AddressListFragment.googleAddressList.addAll(googleAddressList)
                    createAddressListAdapter()
                    swipeRefreshLayout?.isRefreshing = false
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

                    if (this@AddressListFragment.googleAddressList.size == 0) {
                        prepareFeedbackLayout(title, subtitle,drawable)
                    }else{
                        view?.let { Snackbar.make(it, subtitle, Snackbar.LENGTH_LONG).show() }
                    }

                }
        })


    }

    private fun prepareLoadingLayout() {
        feedbackLayout?.visibility = View.VISIBLE
        feedbackTitleTXV?.visibility = View.VISIBLE
        feedbackTitleTXV?.text = context?.getText(R.string.loading_address_list)
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
        if (this@AddressListFragment.googleAddressList.size == 0)
            prepareLoadingLayout()

        getAddressList()
    }

    override fun onItemSelected(googleAddress: GoogleAddress) {
//        (activity as MainActivity).changeFragment(ProductTypeListFragment.newInstance(googleAddress), true)
    }

}


