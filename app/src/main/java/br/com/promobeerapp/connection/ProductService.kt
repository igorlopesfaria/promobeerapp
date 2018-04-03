package br.com.promobeerapp.connection

import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {

    @GET("brands")
    fun listBrands() : Call<RestResponse<List<ProductBrand>>>

    @GET("types")
    fun listTypeByBrandId(@Query("brand") idBrand:Long) : Call<RestResponse<List<ProductType>>>

}