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
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductSize
import br.com.promobeerapp.model.ProductType
import kotlinx.android.synthetic.main.item_product_size_list.view.*

class ProductSizeListAdapter(
        val productSizeList: List<ProductSize>,
        val context: Context?,
        val fragment:Fragment) : Adapter<ProductSizeListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTXV = itemView.descriptionTXV
        val containerView = itemView.containerView
        val checkIMG = itemView.checkIMG
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productSizeItem = productSizeList[position]

        holder.descriptionTXV?.text = productSizeItem.material + " " + productSizeItem.value
        holder.containerView.setOnClickListener {
            (fragment as OnItemSelectedListener<ProductSize>).onItemSelected(productSizeItem)
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_size_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productSizeList.size
    }
}