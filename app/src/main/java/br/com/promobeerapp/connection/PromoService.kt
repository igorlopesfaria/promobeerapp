package br.com.promobeerapp.connection

import br.com.promobeerapp.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PromoService {

    @GET("promotions")
    fun list(@Query("limit") limit:Int,
             @Query("order") order:String,
             @Query("orderDirection") orderDirection:String
             ) : Call<RestResponse<List<Promo>>>

    @GET("promotions")
    fun listByFilter(@Query("limit") limit:Int,
                     @Query("order") order:String,
                     @Query("orderDirection") orderDirection:String,
                     @Query("brand") idBrand:Long,
                     @Query("type") idType:Long) : Call<RestResponse<List<Promo>>>


}//limit=5&page=1&order=created_at&orderDirection=desc