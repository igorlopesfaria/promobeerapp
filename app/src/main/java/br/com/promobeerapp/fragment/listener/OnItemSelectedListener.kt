package br.com.promobeerapp.fragment.listener

import br.com.promobeerapp.model.ProductBrand

interface OnItemSelectedListener<T> {
    fun onItemSelected(t: T)

}