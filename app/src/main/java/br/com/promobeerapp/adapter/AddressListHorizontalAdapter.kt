package br.com.promobeerapp.adapter

/**
 * Created by root on 15/03/18.
 */
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.R
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.GoogleAddress
import kotlinx.android.synthetic.main.item_address_list_horizontal.view.*

class AddressListHorizontalAdapter(
        val googleAddressList: List<GoogleAddress>,
        val context: Context?,
        val fragment: Fragment) : Adapter<AddressListHorizontalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailTXV = itemView.detailTXV;
        val descriptionTXV = itemView.descriptionTXV
        val containerView = itemView.containerView

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val googleAddress = googleAddressList[position]


        holder.descriptionTXV?.text = googleAddress.name
        holder.detailTXV?.text = googleAddress.vicinity.split(",")[0]

        holder.containerView.setOnClickListener {
            (fragment as OnItemSelectedListener<GoogleAddress>).onItemSelected(googleAddress)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address_list_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return googleAddressList.size
    }
}