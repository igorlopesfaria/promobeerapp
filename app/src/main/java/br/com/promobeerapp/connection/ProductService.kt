package br.com.promobeerapp.connection

import br.com.promobeerapp.model.Product
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductSize
import br.com.promobeerapp.model.ProductType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {

    @GET("brands")
    fun listBrands() : Call<RestResponse<List<ProductBrand>>>

    @GET("types")
    fun listTypeByFilter(@Query("brand") idBrand:Long) : Call<RestResponse<List<ProductType>>>

    @GET("sizes")
    fun listSizeByFilter(@Query("brand") idBrand:Long,
                          @Query("type") idType:Long) : Call<RestResponse<List<ProductSize>>>

    @GET("products")
    fun listByFilter(@Query("brand") idBrand:Long,
                         @Query("type") idType:Long,
                         @Query("size") idSize:Long) : Call<RestResponse<List<Product>>>

}