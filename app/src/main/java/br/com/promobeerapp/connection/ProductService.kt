package br.com.promobeerapp.connection

import br.com.promobeerapp.model.ProductBrand
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {

    @GET("brands")
    fun listBrands() : Call<RestResponse<List<ProductBrand>>>


}