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
import br.com.promobeerapp.fragment.listener.OnSelectProductBrandListListener
import br.com.promobeerapp.model.ProductBrand
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product_brand_list.view.*

class ProductBrandListAdapter(
        val productBrandList: List<ProductBrand>,
        val context: Context?,
        val fragment: Fragment) : Adapter<ProductBrandListAdapter.ViewHolder>() {

    var lastItemSelected:Int = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val logoIMG = itemView.logoIMG
        val nameTXV = itemView.nameTXV
        val containerView = itemView.containerView
        val checkIMG = itemView.checkIMG

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brandItem = productBrandList[position]
        holder?.let {
            Picasso.with(context)
                    .load(brandItem.imagePath)
                    .placeholder(R.drawable.ic_bottle_beer)
                    .into(it.logoIMG);
            it.containerView?.setOnClickListener {


                holder.checkIMG.visibility = View.VISIBLE
                brandItem.checked = true

                if (lastItemSelected>=0 && lastItemSelected!=position) {
                    productBrandList[lastItemSelected].checked = !productBrandList[lastItemSelected].checked
                    notifyItemChanged(lastItemSelected)
                }
                lastItemSelected = position
                if(fragment is OnSelectProductBrandListListener)
                    fragment.brandSelected(brandItem)

            }

            if (brandItem.checked)
                it.checkIMG.visibility =View.VISIBLE
            else
                it.checkIMG.visibility =View.GONE
            it.nameTXV?.text = brandItem.name
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