package br.com.promobeerapp.adapter

import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paginate.recycler.LoadingListItemCreator;
import br.com.promobeerapp.R
import kotlinx.android.synthetic.main.item_loading.view.*

/**
 * Created by root on 20/03/18.
 */
class CustomLoadingListItemCreator : LoadingListItemCreator {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


     override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_loading, parent, false)
         return CustomLoadingListItemCreator.ViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}

