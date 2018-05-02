package br.com.promobeerapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.Toast
import br.com.promobeerapp.BaseActivity

import br.com.promobeerapp.R
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductSize
import br.com.promobeerapp.model.ProductType
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
                (activity as BaseActivity).preffs?.setBrandFilter(null)
                (activity as BaseActivity).preffs?.setTypeFilter(null)
                (activity as BaseActivity).preffs?.setSizeFilter(null)
                ProductBrandListFragment.productBrandSelected = null
                ProductTypeListFragment.productTypeSelected = null
                ProductSizeListFragment.productSizeSelected = null
                (activity as BaseActivity).preffs?.setRefrashPromoList(true)

                brandTXV.text = getString(R.string.select)
                typeTXV.text = getString(R.string.select)
                sizeTXV.text = getString(R.string.select)

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
            (activity as BaseActivity).preffs?.setBrandFilter(ProductBrandListFragment.productBrandSelected)
            (activity as BaseActivity).preffs?.setTypeFilter(ProductTypeListFragment.productTypeSelected)
            (activity as BaseActivity).preffs?.setSizeFilter(ProductSizeListFragment.productSizeSelected)
            (activity as BaseActivity).onBackPressed()

        }

        brandTXV.setOnClickListener {
            (activity as BaseActivity).changeFragment(ProductBrandListFragment.newInstance(true),true)
        }

        typeTXV.setOnClickListener {
            (activity as BaseActivity).changeFragment(ProductTypeListFragment.newInstance(null,true),true)
        }

        sizeTXV.setOnClickListener {
            (activity as BaseActivity).changeFragment(ProductSizeListFragment.newInstance(null,null, true),true)
        }

        filterBTN.setOnClickListener{
            (activity as BaseActivity).preffs?.setBrandFilter(ProductBrandListFragment.productBrandSelected)
            (activity as BaseActivity).preffs?.setTypeFilter(ProductTypeListFragment.productTypeSelected)
            (activity as BaseActivity).preffs?.setSizeFilter(ProductSizeListFragment.productSizeSelected)
            (activity as BaseActivity).preffs?.setRefrashPromoList(true)
            activity?.onBackPressed()

        }

        val productBrandFiltered:ProductBrand?
        if(ProductBrandListFragment.productBrandSelected==null){
            productBrandFiltered = (activity as BaseActivity).preffs?.getBrandFilter()
        }else{
            productBrandFiltered = ProductBrandListFragment.productBrandSelected
        }

        val productTypeFiltered:ProductType?
        if(ProductTypeListFragment.productTypeSelected==null){
            productTypeFiltered = (activity as BaseActivity).preffs?.getTypeFilter()
        }else{
            productTypeFiltered = ProductTypeListFragment.productTypeSelected
        }

        val productSizeFiltered: ProductSize?
        if(ProductSizeListFragment.productSizeSelected==null){
            productSizeFiltered = (activity as BaseActivity).preffs?.getSizeFilter()
        }else{
            productSizeFiltered = ProductSizeListFragment.productSizeSelected
        }


        if(productBrandFiltered!=null)
            brandTXV.text = productBrandFiltered.name

        if(productTypeFiltered!=null)
            typeTXV.text = productTypeFiltered.name

        if(productSizeFiltered!=null)
            sizeTXV.text = productSizeFiltered.material + ": "+productSizeFiltered.value+"ml"

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
