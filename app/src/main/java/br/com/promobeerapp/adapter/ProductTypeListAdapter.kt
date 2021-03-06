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
import br.com.promobeerapp.model.ProductType
import kotlinx.android.synthetic.main.item_product_size_list.view.*

class ProductTypeListAdapter(
        val productTypeList: List<ProductType>,
        val context: Context?,
        val fragment: Fragment) : Adapter<ProductTypeListAdapter.ViewHolder>() {

//    var lastItemSelected: Int = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTXV = itemView.descriptionTXV
        val containerView = itemView.containerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productTypeItem = productTypeList[position]
        holder.descriptionTXV?.text = productTypeItem.name
        holder.containerView.setOnClickListener {
            (fragment as OnItemSelectedListener<ProductType>).onItemSelected(productTypeItem)
        }

//        holder.containerView?.setOnClickListener {
//
//            holder.checkIMG.visibility = View.VISIBLE
//            productTypeItem.checked = true
//
//            if (lastItemSelected >= 0 && lastItemSelected != position) {
//                productTypeList[lastItemSelected].checked = !productTypeList[lastItemSelected].checked
//                notifyItemChanged(lastItemSelected)
//            }
//            lastItemSelected = position
//        }

//        if (productTypeItem.checked)
//            holder.checkIMG.visibility = View.VISIBLE
//        else
//            holder.checkIMG.visibility = View.GONE


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_type_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productTypeList.size
    }
}