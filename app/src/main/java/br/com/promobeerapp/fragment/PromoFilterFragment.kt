package br.com.promobeerapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.Toast
import br.com.promobeerapp.MainActivity

import br.com.promobeerapp.R
import br.com.promobeerapp.model.enum.OrderFilterState
import kotlinx.android.synthetic.main.fragment_promo_filter.*


class PromoFilterFragment : Fragment() {

    var orderFilterState: OrderFilterState = OrderFilterState.BY_DATE
    companion object {
        fun newInstance(): PromoFilterFragment {
            return PromoFilterFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.item_menu_clear ->{
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_promo_filter, container,
                false)
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_promo_filter, menu)
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selecOrderByButton()
        orderByDateBTN.setOnClickListener {
            orderFilterState = OrderFilterState.BY_DATE
            selecOrderByButton()
        }

        orderByPriceBTN.setOnClickListener {
            orderFilterState = OrderFilterState.BY_PRICE
            selecOrderByButton()

        }

        filterBTN.setOnClickListener{
            activity?.onBackPressed()

        }
    }

    fun selecOrderByButton(){
        context?.let{
            if(orderFilterState == OrderFilterState.BY_DATE){
                orderByPriceBTN.background = ContextCompat.getDrawable(it, R.drawable.option_not_select);
                orderByPriceBTN.setTextColor(ContextCompat.getColor(it, R.color.line_list));
                orderByDateBTN.background = ContextCompat.getDrawable(it, R.drawable.option_select);
                orderByDateBTN.setTextColor(ContextCompat.getColor(it, android.R.color.white));
            }else{
                orderByPriceBTN.background = ContextCompat.getDrawable(it, R.drawable.option_select);
                orderByPriceBTN.setTextColor(ContextCompat.getColor(it, android.R.color.white));
                orderByDateBTN.background = ContextCompat.getDrawable(it, R.drawable.option_not_select);
                orderByDateBTN.setTextColor(ContextCompat.getColor(it, R.color.line_list));
            }
        }
    }



}
