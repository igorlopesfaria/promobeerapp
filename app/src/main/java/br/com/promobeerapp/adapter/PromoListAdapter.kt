package br.com.promobeerapp.adapter

/**
 * Created by root on 15/03/18.
 */
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.R
import br.com.promobeerapp.model.Promo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_promo_list.view.*

class PromoListAdapter(
        val promoList: List<Promo>,
        val context: Context?) : Adapter<PromoListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productIMG = itemView.productIMG
        val priceTXV = itemView.priceTXV
        val productSizeTXV = itemView.productSizeTXV
        val productNameTXV = itemView.productNameTXV
        val publishedDateTXV = itemView.publishedDateTXV
        val neighbhoodTXV = itemView.neighbhoodTXV

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promoItem = promoList[position]

        Picasso.with(context)
                .load(promoItem?.product?.imagePath)
                .placeholder(R.drawable.ic_bottle_beer)
                .into(holder.productIMG);
        holder.productNameTXV.text = promoItem?.product?.productBrand?.name + " " +
                promoItem?.product?.productType?.name
        holder.priceTXV.text = promoItem?.price
        holder.productSizeTXV.text = promoItem?.product?.productSize?.material + ": " + promoItem?.product?.productSize?.value
        holder.publishedDateTXV.text = context?.getText(R.string.publishedDate) as String + " " + promoItem?.publishedDate
        holder.neighbhoodTXV.text = promoItem?.address?.neighborhood


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_promo_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return promoList?.size
    }

    fun getCount(): Int {
        return promoList?.size
    }
}