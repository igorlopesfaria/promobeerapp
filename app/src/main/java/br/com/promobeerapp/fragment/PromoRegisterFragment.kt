package br.com.promobeerapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.MainActivity

import br.com.promobeerapp.R
import br.com.promobeerapp.model.ProductBrand
import kotlinx.android.synthetic.main.fragment_promo_register.*


class PromoRegisterFragment : Fragment() {

    companion object {
        fun newInstance(): PromoRegisterFragment {
            return PromoRegisterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_promo_register, container,
                false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brandTXV.setOnClickListener{
            if (activity is MainActivity)
                (activity as MainActivity).changeFragment(ProductBrandListFragment.newInstance(), true)
        }
    }

}
