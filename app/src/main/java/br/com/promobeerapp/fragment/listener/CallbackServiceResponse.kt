package br.com.promobeerapp.fragment.listener

import br.com.promobeerapp.model.ProductBrand

interface CallbackServiceResponse<T> {
    fun success(response: T)

    fun fail(throwable:Throwable)

}