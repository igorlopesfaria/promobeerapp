package br.com.promobeerapp.adapter

/**
 * Created by root on 15/03/18.
 */
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import br.com.promobeerapp.R
import br.com.promobeerapp.R.id.containerView
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.ProductBrand
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_brand_list.view.*

class ProductBrandListAdapter(
        val productBrandList: List<ProductBrand>,
        val context: Context?,
        val fragment: Fragment) : Adapter<ProductBrandListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val logoIMG = itemView.logoIMG
        val containerView = itemView.containerView

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brandItem = productBrandList[position]

        Picasso.with(context)
                .load(brandItem.imagePath)
                .into(holder.logoIMG)

        holder.containerView.setOnClickListener {
            (fragment as OnItemSelectedListener<ProductBrand>).onItemSelected(brandItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_brand_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productBrandList.size
    }
}